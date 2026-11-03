package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.*;
import vn.taphoagan.repository.*;
import vn.taphoagan.web.BusinessException;
import vn.taphoagan.web.NotFoundException;
import vn.taphoagan.web.form.ActionForms;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static vn.taphoagan.domain.Enums.*;

@Service
public class PosService {

    private final PosSessionRepository sessions;
    private final PosLineRepository lines;
    private final ProductRepository products;
    private final CatalogService catalog;
    private final StockService stock;
    private final OrderRepository orders;
    private final OrderItemRepository orderItems;
    private final OrderEventRepository events;
    private final StoreService storeService;

    public PosService(PosSessionRepository sessions, PosLineRepository lines, ProductRepository products,
                      CatalogService catalog, StockService stock, OrderRepository orders,
                      OrderItemRepository orderItems, OrderEventRepository events, StoreService storeService) {
        this.sessions = sessions;
        this.lines = lines;
        this.products = products;
        this.catalog = catalog;
        this.stock = stock;
        this.orders = orders;
        this.orderItems = orderItems;
        this.events = events;
        this.storeService = storeService;
    }

    public PosSession currentSession(Long storeId) {
        return sessions.findFirstByStoreIdAndStatus(storeId, PosSessionStatus.OPEN).orElse(null);
    }

    @Transactional
    public PosSession open(Long storeId) {
        PosSession existing = currentSession(storeId);
        if (existing != null) {
            return existing;
        }
        PosSession session = new PosSession();
        session.setStoreId(storeId);
        session.setStatus(PosSessionStatus.OPEN);
        return sessions.save(session);
    }

    @Transactional
    public void cancel(Long storeId) {
        PosSession session = currentSession(storeId);
        if (session != null) {
            session.setStatus(PosSessionStatus.CLOSED);
            session.setUpdatedAt(Instant.now());
            sessions.save(session);
        }
    }

    public List<PosLine> lines(Long sessionId) {
        return lines.findBySessionId(sessionId);
    }

    public long total(Long sessionId) {
        return lines.findBySessionId(sessionId).stream()
                .mapToLong(l -> l.getUnitPriceSnapshot() * l.getQuantity()).sum();
    }

    @Transactional
    public PosLine addByBarcode(Long storeId, String barcode) {
        Product product = catalog.findByBarcode(storeId, barcode);
        if (product == null) {
            throw new NotFoundException("Mã chưa có trong danh mục.");
        }
        return addProduct(storeId, product.getId());
    }

    @Transactional
    public PosLine addProduct(Long storeId, Long productId) {
        PosSession session = open(storeId);
        Product product = catalog.require(storeId, productId);
        PosLine line = lines.findBySessionId(session.getId()).stream()
                .filter(l -> productId.equals(l.getProductId()))
                .findFirst().orElse(null);
        if (line == null) {
            line = new PosLine();
            line.setSessionId(session.getId());
            line.setProductId(product.getId());
            line.setNameSnapshot(product.getName());
            line.setUnitPriceSnapshot(product.getPrice());
            line.setUnitSnapshot(product.getUnit());
            line.setQuantity(1);
        } else {
            line.setQuantity(line.getQuantity() + 1);
        }
        return lines.save(line);
    }

    @Transactional
    public PosLine quickCreate(Long storeId, ActionForms.QuickCreateForm form) {
        Product product = catalog.quickCreate(storeId, form.getBarcode(), form.getName(),
                form.getPrice(), form.getUnit(), form.getStock());
        return addProduct(storeId, product.getId());
    }

    @Transactional
    public void updateLine(Long storeId, Long lineId, int quantity) {
        PosLine line = lines.findById(lineId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy dòng bán."));
        PosSession session = sessions.findById(line.getSessionId()).orElseThrow();
        if (!session.getStoreId().equals(storeId)) {
            throw new NotFoundException("Không tìm thấy dòng bán.");
        }
        if (quantity <= 0) {
            lines.delete(line);
        } else {
            line.setQuantity(quantity);
            lines.save(line);
        }
    }

    @Transactional
    public void removeLine(Long storeId, Long lineId) {
        updateLine(storeId, lineId, 0);
    }

    @Transactional
    public Order complete(Long storeId) {
        PosSession session = currentSession(storeId);
        if (session == null) {
            throw new BusinessException("POS_NO_SESSION", "Chưa có phiên bán.");
        }
        List<PosLine> list = lines.findBySessionId(session.getId());
        if (list.isEmpty()) {
            throw new BusinessException("POS_EMPTY", "Hóa đơn đang trống.");
        }
        Store store = storeService.requireById(storeId);
        long total = list.stream().mapToLong(l -> l.getUnitPriceSnapshot() * l.getQuantity()).sum();
        Order order = new Order();
        order.setCode(uniqueCode());
        order.setStoreId(storeId);
        order.setType(OrderType.POS);
        order.setStatus(OrderStatus.COMPLETED);
        order.setFulfillment(Fulfillment.PICKUP);
        order.setSubtotal(total);
        order.setDeliveryFee(0);
        order.setTotal(total);
        order.setPaid(true);
        order = orders.save(order);

        for (PosLine line : list) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(line.getProductId());
            item.setNameSnapshot(line.getNameSnapshot());
            item.setUnitPriceSnapshot(line.getUnitPriceSnapshot());
            item.setUnitSnapshot(line.getUnitSnapshot());
            item.setQuantity(line.getQuantity());
            item.setPickedQuantity(line.getQuantity());
            orderItems.save(item);
            if (line.getProductId() != null) {
                stock.decrementForPos(line.getProductId(), line.getQuantity(), order.getId());
            }
        }
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setToStatus(OrderStatus.COMPLETED);
        event.setNote("Bán tại quầy");
        events.save(event);

        session.setStatus(PosSessionStatus.CLOSED);
        session.setUpdatedAt(Instant.now());
        sessions.save(session);
        lines.deleteAll(list);
        return order;
    }

    public Order receipt(String code, Store store) {
        Order order = orders.findByCodeAndStoreId(code, store.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn."));
        return order;
    }

    private String uniqueCode() {
        String date = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDate.now(StoreService.ZONE));
        for (int i = 0; i < 20; i++) {
            String code = "POS-" + date + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
            if (orders.findByCode(code).isEmpty()) {
                return code;
            }
        }
        throw new BusinessException("POS_CODE", "Không tạo được mã hóa đơn.");
    }
}
