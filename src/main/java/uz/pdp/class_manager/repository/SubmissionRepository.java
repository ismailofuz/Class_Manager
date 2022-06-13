package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
}