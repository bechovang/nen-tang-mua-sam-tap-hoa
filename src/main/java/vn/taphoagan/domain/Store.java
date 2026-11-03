package vn.taphoagan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

import static vn.taphoagan.domain.Enums.StoreStatus;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false)
    private String address;

    @Column(name = "ward_id")
    private Long wardId;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "radius_km", nullable = false, precision = 4, scale = 1)
    private BigDecimal radiusKm;

    @Column(name = "delivery_enabled", nullable = false)
    private boolean deliveryEnabled = true;

    @Column(name = "pickup_enabled", nullable = false)
    private boolean pickupEnabled = true;

    @Column(name = "delivery_fee", nullable = false)
    private long deliveryFee;

    @Column(name = "min_order", nullable = false)
    private long minOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StoreStatus status = StoreStatus.OPEN;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();
}
