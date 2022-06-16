package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}