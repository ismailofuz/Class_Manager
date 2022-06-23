package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.class_manager.entity.Assignment;
import uz.pdp.class_manager.entity.Attachment;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.AssignmentDTO;
import uz.pdp.class_manager.repository.AssignmentRepository;
import uz.pdp.class_manager.repository.AttachmentRepository;
import uz.pdp.class_manager.repository.ClassRepository;
import uz.pdp.class_manager.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    private final AttachmentRepository attachmentRepository;

    private final ClassRepository classRepository;
    public ApiResponse getAssignment(Integer id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);
        return optionalAssignment.map(assignment -> new ApiResponse("", true, assignment)).orElseGet(() -> new ApiResponse("This assignment not found", false));
    }

//    public List<Assignment> getAssignments(Classes classes) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User) authentication.getPrincipal();
//        List<Assignment> assignments = new ArrayList<>();
//        for (Classes aClass : principal.getClasses()) {
//            if (aClass.getName().equals(classes.getName())) {
//                List<Assignment> allByClassesName = assignmentRepository.findAllByClassesName(aClass.getName());
//                assignments.addAll(allByClassesName);
//            }
//        }
//        return assignments;
//    }

    public ApiResponse addAssignment(AssignmentDTO assignmentDTO){
        Optional<User> user = userRepository.findById(assignmentDTO.getTeacherId());
        Optional<Attachment> attachment = attachmentRepository.findById(assignmentDTO.getAttachmentId());
        Optional<Classes> classes = classRepository.findById(assignmentDTO.getClassId());

        Assignment assignment = new Assignment();
        assignment.setUser(user.get());
        assignment.setClasses(classes.get());
        assignment.setAttachment(attachment.get());
        assignmentRepository.save(assignment);

        return new ApiResponse("Successfully added new assignment", true);
    }
}
