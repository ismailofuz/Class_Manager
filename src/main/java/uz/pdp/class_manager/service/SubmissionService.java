package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Attachment;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.Submission;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.SubmissionDTO;
import uz.pdp.class_manager.repository.AttachmentRepository;
import uz.pdp.class_manager.repository.ClassRepository;
import uz.pdp.class_manager.repository.SubmissionRepository;
import uz.pdp.class_manager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    private final AttachmentRepository attachmentRepository;

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

    public ApiResponse  addSubmission(SubmissionDTO submissionDTO) {
        Optional<Classes> classes = classRepository.findById(submissionDTO.getClassId());
        Optional<User> user = userRepository.findById(submissionDTO.getStudentId());
        Optional<Attachment> attachment = attachmentRepository.findById(submissionDTO.getAttachmentId());

        Submission submission = new Submission();

        submission.setAttachment(attachment.get());
        submission.setStudent(user.get());
        submission.setCourseWork(classes.get());

        submissionRepository.save(submission);

        return new ApiResponse("Submission added successfully!", true);
    }
}
