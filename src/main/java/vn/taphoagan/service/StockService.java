package vn.taphoagan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.taphoagan.domain.OrderItem;
import vn.taphoagan.domain.Product;
import vn.taphoagan.domain.StockEvent;
import vn.taphoagan.repository.OrderItemRepository;
import vn.taphoagan.repository.ProductRepository;
import vn.taphoagan.repository.StockEventRepository;

import java.util.List;

import static vn.taphoagan.domain.Enums.StockReason;

@Service
public class StockService {

    private final ProductRepository products;
    private final StockEventRepository events;
    private final OrderItemRepository orderItems;

    public StockService(ProductRepository products, StockEventRepository events, OrderItemRepository orderItems) {
        this.products = products;
        this.events = events;
        this.orderItems = orderItems;
    }

    @Transactional
    public void reserve(List<OrderItem> items, Long orderId) {
        for (OrderItem item : items) {
            if (item.getProductId() == null || item.isStockReserved()) {
                continue;
            }
            Product product = products.findById(item.getProductId()).orElse(null);
            if (product == null) {
                continue;
            }
            int take = Math.min(product.getStock(), item.getQuantity());
            if (take > 0 && products.decrementStock(product.getId(), take) > 0) {
                event(product.getId(), -take, StockReason.RESERVE, orderId);
                item.setStockReserved(true);
                orderItems.save(item);
            }
        }
    }

    @Transactional
    public void release(OrderItem item, Long orderId) {
        if (!item.isStockReserved() || item.getProductId() == null) {
            return;
        }
        products.incrementStock(item.getProductId(), item.getQuantity());
        event(item.getProductId(), item.getQuantity(), StockReason.RELEASE, orderId);
        item.setStockReserved(false);
        orderItems.save(item);
    }

    @Transactional
    public void release(OrderItem item, int quantity, Long orderId, StockReason reason) {
        if (quantity <= 0) {
            return;
        }
        products.incrementStock(item.getProductId(), quantity);
        event(item.getProductId(), quantity, reason, orderId);
    }

    @Transactional
    public void decrementForPos(Long productId, int quantity, Long orderId) {
        if (quantity <= 0) {
            return;
        }
        products.decrementStock(productId, quantity);
        event(productId, -quantity, StockReason.POS_SALE, orderId);
    }

    private void event(Long productId, int delta, StockReason reason, Long orderId) {
        StockEvent event = new StockEvent();
        event.setProductId(productId);
        event.setDelta(delta);
        event.setReason(reason);
        event.setOrderId(orderId);
        events.save(event);
    }
}
