package vn.taphoagan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.taphoagan.domain.Enums;
import vn.taphoagan.domain.Order;
import vn.taphoagan.repository.OrderRepository;

import java.time.Instant;
import java.util.List;

import static vn.taphoagan.domain.Enums.OrderStatus;

@Component
public class PickupExpiryJob {

    private static final Logger log = LoggerFactory.getLogger(PickupExpiryJob.class);
    private static final long LOCK_ID = 424242L;

    private final JdbcTemplate jdbc;
    private final OrderRepository orders;
    private final OrderService orderService;
    private final NotificationService notifications;

    public PickupExpiryJob(JdbcTemplate jdbc, OrderRepository orders, OrderService orderService,
                           NotificationService notifications) {
        this.jdbc = jdbc;
        this.orders = orders;
        this.orderService = orderService;
        this.notifications = notifications;
    }

    @Scheduled(fixedDelay = 300_000, initialDelay = 60_000)
    public void expirePickups() {
        Boolean locked = jdbc.queryForObject("select pg_try_advisory_lock(?)", Boolean.class, LOCK_ID);
        if (!Boolean.TRUE.equals(locked)) {
            return;
        }
        try {
            Instant now = Instant.now();
            List<Order> reminders = orders.findByStatusAndPickupEndBetweenAndReminderSentFalse(
                    OrderStatus.READY, now, now.plusSeconds(1800));
            for (Order order : reminders) {
                order.setReminderSent(true);
                orders.save(order);
                notifications.notifyCustomer(order, Enums.NotificationType.PICKUP_REMINDER,
                        "Sắp tới giờ lấy hàng",
                        "Đơn " + order.getCode() + " còn khoảng 30 phút giữ hàng tại tiệm.");
            }
            List<Order> expired = orders.findByStatusAndHoldUntilBefore(OrderStatus.READY, now);
            for (Order order : expired) {
                try {
                    orderService.transition(order, OrderStatus.NO_SHOW, null, "Quá hạn nhận tại tiệm");
                    log.info("Pickup order {} marked NO_SHOW", order.getCode());
                } catch (Exception ex) {
                    log.warn("Could not expire order {}: {}", order.getCode(), ex.getMessage());
                }
            }
        } finally {
            jdbc.queryForObject("select pg_advisory_unlock(?)", Boolean.class, LOCK_ID);
        }
    }
}
