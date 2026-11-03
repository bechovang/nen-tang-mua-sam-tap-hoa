package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.StoreHour;

import java.util.List;

public interface StoreHourRepository extends JpaRepository<StoreHour, Long> {
    List<StoreHour> findByStoreId(Long storeId);
}
