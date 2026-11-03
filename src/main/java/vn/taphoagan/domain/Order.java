package vn.taphoagan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import static vn.taphoagan.domain.Enums.Fulfillment;
import static vn.taphoagan.domain.Enums.OrderStatus;
import static vn.taphoagan.domain.Enums.OrderType;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 24)
    private String code;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "customer_id")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Fulfillment fulfillment;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    private String phone;

    private String note;

    @Column(nullable = false)
    private long subtotal;

    @Column(name = "delivery_fee", nullable = false)
    private long deliveryFee;

    @Column(nullable = false)
    private long total;

    @Column(name = "pickup_start")
    private Instant pickupStart;

    @Column(name = "pickup_end")
    private Instant pickupEnd;

    @Column(name = "hold_until")
    private Instant holdUntil;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent;

    @Column(name = "idempotency_key", length = 64)
    private String idempotencyKey;

    @Column(nullable = false)
    private boolean paid;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
