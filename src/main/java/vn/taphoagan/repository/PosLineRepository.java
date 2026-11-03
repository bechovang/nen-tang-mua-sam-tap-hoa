package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.PosLine;

import java.util.List;

public interface PosLineRepository extends JpaRepository<PosLine, Long> {
    List<PosLine> findBySessionId(Long sessionId);
}
