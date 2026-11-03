package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.Order;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static vn.taphoagan.domain.Enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<Order> findByStoreIdOrderByCreatedAtDesc(Long storeId);

    Optional<Order> findByCode(String code);

    Optional<Order> findByCodeAndCustomerId(String code, Long customerId);

    Optional<Order> findByCodeAndStoreId(String code, Long storeId);

    Optional<Order> findByCustomerIdAndIdempotencyKey(Long customerId, String idempotencyKey);

    List<Order> findByStatusAndHoldUntilBefore(OrderStatus status, Instant now);

    List<Order> findByStatusAndPickupEndBetweenAndReminderSentFalse(OrderStatus status, Instant from, Instant to);

    long countByStoreIdAndStatus(Long storeId, OrderStatus status);
}
