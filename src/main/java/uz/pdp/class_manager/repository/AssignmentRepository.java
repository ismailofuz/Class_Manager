package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
}