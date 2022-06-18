package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.Submission;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.SubmissionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public List<Submission> getSubmissions(Classes classes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        List<Submission> submissions = new ArrayList<>();
        for (Classes classs : principal.getClasses()) {
            if (classes.getName().equals(classs.getName())) {
                List<Submission> allByCourseWorkName = submissionRepository.findAllByCourseWorkName(classes.getName());
                submissions.addAll(allByCourseWorkName);
            }
        }
        return submissions;
    }

    public ApiResponse getSubmission(Integer id) {
        Optional<Submission> optional = submissionRepository.findById(id);
        return optional.map(submission -> new ApiResponse("", true, submission)).orElseGet(() -> new ApiResponse("Submission not found", false));
    }
}
