package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.BarcodeCache;

public interface BarcodeCacheRepository extends JpaRepository<BarcodeCache, String> {
}
