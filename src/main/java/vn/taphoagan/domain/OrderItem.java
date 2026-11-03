package vn.taphoagan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name_snapshot", nullable = false, length = 120)
    private String nameSnapshot;

    @Column(name = "unit_price_snapshot", nullable = false)
    private long unitPriceSnapshot;

    @Column(name = "unit_snapshot", nullable = false, length = 20)
    private String unitSnapshot;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "picked_quantity", nullable = false)
    private int pickedQuantity;

    @Column(name = "stock_reserved", nullable = false)
    private boolean stockReserved;
}
