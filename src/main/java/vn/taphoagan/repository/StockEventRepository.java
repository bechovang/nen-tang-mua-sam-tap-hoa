package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.StockEvent;

public interface StockEventRepository extends JpaRepository<StockEvent, Long> {
}
