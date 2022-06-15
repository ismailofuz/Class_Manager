package uz.pdp.class_manager.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.class_manager.entity.Classes;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Classes, Integer> {
    Optional<Classes> findByName(@NotBlank(message = "Please enter class name!") String name);
}
