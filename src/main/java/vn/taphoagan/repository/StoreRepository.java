package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.taphoagan.domain.Store;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByOwnerId(Long ownerId);

    Optional<Store> findBySlug(String slug);

    boolean existsByOwnerId(Long ownerId);

    List<Store> findByNameContainingIgnoreCase(String name);

    @Query(value = """
            SELECT s.* FROM stores s
            WHERE s.status = 'OPEN'
              AND s.lat BETWEEN :lat - :latDelta AND :lat + :latDelta
              AND s.lng BETWEEN :lng - :lngDelta AND :lng + :lngDelta
              AND distance_km(:lat, :lng, s.lat, s.lng) <= :radiusKm
              AND EXISTS (
                  SELECT 1 FROM products p
                  WHERE p.store_id = s.id AND p.active = true
                    AND p.deleted_at IS NULL AND p.stock > 0
              )
            ORDER BY distance_km(:lat, :lng, s.lat, s.lng)
            LIMIT 50
            """, nativeQuery = true)
    List<Store> findNearby(@Param("lat") double lat,
                           @Param("lng") double lng,
                           @Param("latDelta") double latDelta,
                           @Param("lngDelta") double lngDelta,
                           @Param("radiusKm") double radiusKm);
}
