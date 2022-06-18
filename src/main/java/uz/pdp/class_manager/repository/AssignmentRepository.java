package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.Assignment;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findAllByClassesName(@NotBlank(message = "Please enter class name!") String classes_name);
}