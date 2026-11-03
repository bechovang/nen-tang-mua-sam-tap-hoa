package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.taphoagan.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStoreIdAndDeletedAtIsNullOrderByUpdatedAtDesc(Long storeId);

    Optional<Product> findByStoreIdAndBarcode(Long storeId, String barcode);

    Optional<Product> findByIdAndStoreId(Long id, Long storeId);

    long countByStoreIdAndDeletedAtIsNull(Long storeId);

    long countByStoreIdAndActiveTrueAndDeletedAtIsNull(Long storeId);

    @Query("""
            select p from Product p
            where p.storeId = :storeId and p.deletedAt is null
              and (lower(p.name) like lower(concat('%', :q, '%')) or p.barcode like concat('%', :q, '%'))
            order by p.updatedAt desc
            """)
    List<Product> search(@Param("storeId") Long storeId, @Param("q") String q);

    @Query("""
            select p from Product p
            where p.deletedAt is null and p.active = true
              and lower(p.name) like lower(concat('%', :q, '%'))
            order by p.updatedAt desc
            """)
    List<Product> searchAll(@Param("q") String q);

    @Query("""
            select p from Product p
            where p.deletedAt is null and p.active = true and p.stock > 0
            order by p.updatedAt desc
            """)
    List<Product> findSellable();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            update products
            set stock = stock - :qty, updated_at = now()
            where id = :id and stock >= :qty
            """, nativeQuery = true)
    int decrementStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            update products
            set stock = stock + :qty, updated_at = now()
            where id = :id
            """, nativeQuery = true)
    int incrementStock(@Param("id") Long id, @Param("qty") int qty);
}
