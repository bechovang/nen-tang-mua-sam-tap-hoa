package vn.taphoagan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "barcode_cache")
@Getter
@Setter
@NoArgsConstructor
public class BarcodeCache {

    @Id
    @Column(length = 32)
    private String barcode;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, length = 32)
    private String source;

    @Column(name = "fetched_at", nullable = false)
    private Instant fetchedAt = Instant.now();
}
