package vn.taphoagan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_hours")
@Getter
@Setter
@NoArgsConstructor
public class StoreHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    @Column(name = "open_minute", nullable = false)
    private int openMinute;

    @Column(name = "close_minute", nullable = false)
    private int closeMinute;
}
