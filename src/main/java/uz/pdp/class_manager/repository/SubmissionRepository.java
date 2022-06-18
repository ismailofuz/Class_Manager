package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.Submission;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findAllByCourseWorkName(@NotBlank(message = "Please enter class name!") String courseWork_name);
}