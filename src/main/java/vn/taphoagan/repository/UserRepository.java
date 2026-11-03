package vn.taphoagan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.taphoagan.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuth0Sub(String auth0Sub);
}
