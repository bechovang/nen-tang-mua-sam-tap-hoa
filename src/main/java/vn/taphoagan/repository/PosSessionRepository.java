package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.PosSession;

import java.util.Optional;

import static vn.taphoagan.domain.Enums.PosSessionStatus;

public interface PosSessionRepository extends JpaRepository<PosSession, Long> {
    Optional<PosSession> findFirstByStoreIdAndStatus(Long storeId, PosSessionStatus status);
}
