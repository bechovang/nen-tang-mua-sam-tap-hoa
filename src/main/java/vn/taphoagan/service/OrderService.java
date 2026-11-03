package vn.taphoagan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.*;
import vn.taphoagan.repository.*;
import vn.taphoagan.web.BusinessException;
import vn.taphoagan.web.NotFoundException;
import vn.taphoagan.web.form.CheckoutForm;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static vn.taphoagan.domain.Enums.*;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final OrderItemRepository orderItems;
    private final OrderEventRepository events;
    private final ProductRepository products;
    private final StoreRepository stores;
    private final StoreService storeService;
    private final StockService stock;
    private final NotificationService notifications;
    private final GeoService geo;
    private final int holdHours;

    public OrderService(OrderRepository orders, OrderItemRepository orderItems, OrderEventRepository events,
                        ProductRepository products, StoreRepository stores, StoreService storeService,
                        StockService stock, NotificationService notifications, GeoService geo,
                        @Value("${app.pickup-hold-hours:2}") int holdHours) {
        this.orders = orders;
        this.orderItems = orderItems;
        this.events = events;
        this.products = products;
        this.stores = stores;
        this.storeService = storeService;
        this.stock = stock;
        this.notifications = notifications;
        this.geo = geo;
        this.holdHours = holdHours;
    }

    @Transactional
    public Order placeOrder(User customer, Cart cart, CheckoutForm form) {
        if (cart.isEmpty()) {
            throw new BusinessException("CART_EMPTY", "Giỏ hàng đang trống.");
        }
        if (form.getIdempotencyKey() != null && !form.getIdempotencyKey().isBlank()) {
            var existing = orders.findByCustomerIdAndIdempotencyKey(customer.getId(), form.getIdempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        Store store = stores.findById(cart.getStoreId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy cửa hàng."));
        if (!storeService.isOpenNow(store)) {
            throw new BusinessException("STORE_CLOSED", "Tiệm đang đóng. Vui lòng quay lại sau.");
        }

        Fulfillment fulfillment = form.getFulfillment() == null ? Fulfillment.DELIVERY : form.getFulfillment();
        if (fulfillment == Fulfillment.DELIVERY) {
            if (!store.isDeliveryEnabled()) {
                throw new BusinessException("DELIVERY_OFF", "Tiệm không nhận giao hàng.");
            }
            if (form.getAddress() == null || form.getAddress().isBlank()) {
                throw new BusinessException("ADDRESS_REQUIRED", "Vui lòng nhập địa chỉ giao hàng.");
            }
            if (form.getLat() == null || form.getLng() == null) {
                throw new BusinessException("LOCATION_REQUIRED", "Vui lòng chọn vị trí giao hàng.");
            }
            double distance = geo.distanceKm(store.getLat(), store.getLng(), form.getLat(), form.getLng());
            if (distance > store.getRadiusKm().doubleValue()) {
                throw new BusinessException("OUT_OF_RADIUS", "Địa chỉ nằm ngoài bán kính giao hàng.");
            }
        } else if (!store.isPickupEnabled()) {
            throw new BusinessException("PICKUP_OFF", "Tiệm không nhận khách đến lấy.");
        }

        List<OrderItem> items = new ArrayList<>();
        long subtotal = 0;
        for (Cart.Item cartItem : cart.getItems()) {
            Product product = products.findByIdAndStoreId(cartItem.getProductId(), store.getId())
                    .orElseThrow(() -> new NotFoundException("Mặt hàng không còn trong danh mục."));
            if (!product.isActive() || product.getStock() <= 0) {
                throw new BusinessException("PRODUCT_UNAVAILABLE", product.getName() + " đã hết hàng.");
            }
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setNameSnapshot(product.getName());
            item.setUnitPriceSnapshot(product.getPrice());
            item.setUnitSnapshot(product.getUnit());
            item.setQuantity(cartItem.getQuantity());
            items.add(item);
            subtotal += product.getPrice() * cartItem.getQuantity();
        }
        if (subtotal < store.getMinOrder()) {
            throw new BusinessException("MIN_ORDER", "Tiệm nhận đơn từ " + store.getMinOrder() + " ₫.");
        }
        long fee = fulfillment == Fulfillment.DELIVERY ? store.getDeliveryFee() : 0;
        Order order = new Order();
        order.setCode(uniqueCode());
        order.setStoreId(store.getId());
        order.setCustomerId(customer.getId());
        order.setType(OrderType.ONLINE);
        order.setStatus(OrderStatus.PENDING);
        order.setFulfillment(fulfillment);
        order.setDeliveryAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setNote(form.getNote());
        order.setSubtotal(subtotal);
        order.setDeliveryFee(fee);
        order.setTotal(subtotal + fee);
        order.setIdempotencyKey(form.getIdempotencyKey());
        if (fulfillment == Fulfillment.PICKUP) {
            LocalDate date = LocalDate.now(StoreService.ZONE);
            LocalTime start = parseSlot(form.getPickupSlot());
            if (start == null) {
                start = LocalTime.now(StoreService.ZONE).plusHours(1).withMinute(0).withSecond(0).withNano(0);
            }
            LocalDateTime startDateTime = LocalDateTime.of(date, start);
            if (startDateTime.isBefore(LocalDateTime.now(StoreService.ZONE))) {
                startDateTime = startDateTime.plusDays(1);
            }
            order.setPickupStart(startDateTime.atZone(StoreService.ZONE).toInstant());
            order.setPickupEnd(startDateTime.plusMinutes(30).atZone(StoreService.ZONE).toInstant());
            order.setHoldUntil(startDateTime.plusMinutes(30).plusHours(holdHours)
                    .atZone(StoreService.ZONE).toInstant());
        }
        order = orders.save(order);
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItems.save(item);
        }
        event(order, null, OrderStatus.PENDING, customer.getId(), "Tạo đơn");
        notifications.notifyStore(store, NotificationType.ORDER_NEW,
                "Đơn mới " + order.getCode(), "Bạn có đơn mới cần xác nhận.");
        notifications.notifyCustomer(order, NotificationType.ORDER_NEW,
                "Đã gửi tới tiệm", "Đơn " + order.getCode() + " đã được gửi tới tiệm.");
        return order;
    }

    @Transactional
    public Order transition(Order order, OrderStatus target, User actor, String note) {
        Order managed = orders.findById(order.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng."));
        if (!managed.getStatus().canTransitionTo(target)) {
            throw new BusinessException("INVALID_TRANSITION",
                    "Không thể chuyển đơn từ " + managed.getStatus() + " sang " + target + ".");
        }
        Long orderId = managed.getId();
        List<OrderItem> items = orderItems.findByOrderId(orderId);
        if (target == OrderStatus.CONFIRMED) {
            stock.reserve(items, orderId);
        }
        if (target == OrderStatus.CANCELLED || target == OrderStatus.NO_SHOW) {
            items.forEach(item -> stock.release(item, orderId));
        }
        if (target == OrderStatus.COMPLETED) {
            managed.setPaid(true);
        }
        OrderStatus from = managed.getStatus();
        managed.setStatus(target);
        managed.setUpdatedAt(Instant.now());
        orders.save(managed);
        event(managed, from, target, actor == null ? null : actor.getId(), note);
        notifyTransition(managed, target, note);
        return managed;
    }

    @Transactional
    public void cancelByCustomer(Order order, User customer) {
        if (!order.getCustomerId().equals(customer.getId())) {
            throw new NotFoundException("Không tìm thấy đơn hàng.");
        }
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException("CANCEL_NOT_ALLOWED", "Đơn đã được soạn, không thể hủy.");
        }
        transition(order, OrderStatus.CANCELLED, customer, "Khách hủy đơn");
    }

    @Transactional
    public void cancelByStore(Order order, User owner, String reason) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException("CANCEL_NOT_ALLOWED", "Đơn đã hoàn tất, không thể hủy.");
        }
        transition(order, OrderStatus.CANCELLED, owner, reason);
    }

    @Transactional
    public Order adjustShortage(Order order, Long itemId, int quantity, User owner) {
        order = orders.findById(order.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng."));
        OrderItem item = orderItems.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy dòng đơn."));
        if (!item.getOrderId().equals(order.getId())) {
            throw new NotFoundException("Không tìm thấy dòng đơn.");
        }
        if (quantity < 0 || quantity > item.getQuantity()) {
            throw new BusinessException("SHORTAGE_INVALID", "Số lượng thực tế không hợp lệ.");
        }
        int difference = item.getQuantity() - quantity;
        if (difference > 0 && item.isStockReserved()) {
            stock.release(item, difference, order.getId(), StockReason.SHORTAGE);
        }
        item.setQuantity(quantity);
        if (quantity == 0) {
            stock.release(item, order.getId());
        }
        orderItems.save(item);
        recalculate(order);
        event(order, order.getStatus(), order.getStatus(), owner.getId(), "Điều chỉnh thiếu hàng");
        notifications.notifyCustomer(order, NotificationType.ORDER_ADJUSTED,
                "Đơn được điều chỉnh", "Tiệm báo thiếu hàng. Vui lòng kiểm tra lại đơn " + order.getCode() + ".");
        return order;
    }

    @Transactional
    public void markPicked(Order order, Long itemId, int picked, User owner) {
        OrderItem item = orderItems.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy dòng đơn."));
        if (!item.getOrderId().equals(order.getId())) {
            throw new NotFoundException("Không tìm thấy dòng đơn.");
        }
        item.setPickedQuantity(Math.min(item.getQuantity(), Math.max(0, picked)));
        orderItems.save(item);
    }

    public List<OrderItem> items(Order order) {
        return orderItems.findByOrderId(order.getId());
    }

    public OrderItem itemByBarcode(Order order, String barcode) {
        if (barcode == null || barcode.isBlank()) {
            return null;
        }
        String normalized = barcode.trim();
        for (OrderItem item : orderItems.findByOrderId(order.getId())) {
            if (item.getProductId() == null) {
                continue;
            }
            Product product = products.findById(item.getProductId()).orElse(null);
            if (product != null && product.getBarcode().equalsIgnoreCase(normalized)) {
                return item;
            }
        }
        return null;
    }

    public List<OrderEvent> events(Order order) {
        return events.findByOrderIdOrderByCreatedAtAsc(order.getId());
    }

    public List<Order> customerOrders(User user) {
        return orders.findByCustomerIdOrderByCreatedAtDesc(user.getId());
    }

    public List<Order> storeOrders(Store store) {
        return orders.findByStoreIdOrderByCreatedAtDesc(store.getId());
    }

    public Order customerOrder(String code, User user) {
        return orders.findByCodeAndCustomerId(code, user.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng."));
    }

    public Order storeOrder(String code, Store store) {
        return orders.findByCodeAndStoreId(code, store.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng."));
    }

    public long countPending(Store store) {
        return orders.countByStoreIdAndStatus(store.getId(), OrderStatus.PENDING);
    }

    public Cart reorder(Order order) {
        Cart cart = new Cart();
        Store store = stores.findById(order.getStoreId()).orElseThrow();
        cart.setStoreId(store.getId());
        cart.setStoreName(store.getName());
        cart.setStoreSlug(store.getSlug());
        for (OrderItem item : orderItems.findByOrderId(order.getId())) {
            if (item.getProductId() == null) {
                continue;
            }
            Product product = products.findByIdAndStoreId(item.getProductId(), store.getId()).orElse(null);
            if (product == null || !product.isActive() || product.getStock() <= 0) {
                continue;
            }
            Cart.Item cartItem = new Cart.Item();
            cartItem.setProductId(product.getId());
            cartItem.setName(product.getName());
            cartItem.setUnit(product.getUnit());
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(item.getQuantity());
            cart.getItems().add(cartItem);
        }
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("REORDER_EMPTY", "Các món trong đơn cũ không còn bán.");
        }
        return cart;
    }

    private void recalculate(Order order) {
        long subtotal = orderItems.findByOrderId(order.getId()).stream()
                .mapToLong(i -> i.getUnitPriceSnapshot() * i.getQuantity()).sum();
        order.setSubtotal(subtotal);
        order.setTotal(subtotal + order.getDeliveryFee());
        order.setUpdatedAt(Instant.now());
        orders.save(order);
    }

    private void notifyTransition(Order order, OrderStatus target, String note) {
        Store store = stores.findById(order.getStoreId()).orElse(null);
        switch (target) {
            case CONFIRMED -> notifications.notifyCustomer(order, NotificationType.ORDER_CONFIRMED,
                    "Tiệm đã xác nhận", "Đơn " + order.getCode() + " đang được chuẩn bị.");
            case CANCELLED -> {
                notifications.notifyCustomer(order, NotificationType.ORDER_CANCELLED,
                        "Đơn đã hủy", "Đơn " + order.getCode() + " đã hủy." + (note == null ? "" : " Lý do: " + note));
                if (store != null) {
                    notifications.notifyStore(store, NotificationType.ORDER_CANCELLED,
                            "Đơn đã hủy", "Đơn " + order.getCode() + " đã hủy.");
                }
            }
            case COMPLETED -> notifications.notifyCustomer(order, NotificationType.ORDER_COMPLETED,
                    "Đơn hoàn tất", "Cảm ơn bạn đã mua tại tiệm.");
            case NO_SHOW -> notifications.notifyCustomer(order, NotificationType.ORDER_COMPLETED,
                    "Đơn quá hạn nhận", "Đơn " + order.getCode() + " đã quá giờ giữ hàng.");
            default -> {
            }
        }
    }

    private void event(Order order, OrderStatus from, OrderStatus to, Long actorId, String note) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setFromStatus(from);
        event.setToStatus(to);
        event.setActorId(actorId);
        event.setNote(note);
        events.save(event);
    }

    private String uniqueCode() {
        String date = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDate.now(StoreService.ZONE));
        for (int i = 0; i < 20; i++) {
            String code = "TH-" + date + "-" + ThreadLocalRandom.current().nextInt(1000, 9999);
            if (orders.findByCode(code).isEmpty()) {
                return code;
            }
        }
        throw new BusinessException("ORDER_CODE", "Không tạo được mã đơn, thử lại.");
    }

    private LocalTime parseSlot(String slot) {
        if (slot == null || slot.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(slot);
        } catch (Exception ex) {
            return null;
        }
    }
}
