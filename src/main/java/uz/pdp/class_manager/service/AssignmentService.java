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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AttachmentRepository attachmentRepository;

    public ApiResponse getAssignment(Integer id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);
        return optionalAssignment.map(assignment -> new ApiResponse("", true, assignment)).orElseGet(() -> new ApiResponse("This assignment not found", false));
    }

    public List<Assignment> getAssignments(Classes classes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        List<Assignment> assignments = new ArrayList<>();
        for (Classes aClass : principal.getClasses()) {
            if (aClass.getName().equals(classes.getName())) {
                List<Assignment> allByClassesName = assignmentRepository.findAllByClassesName(aClass.getName());
                assignments.addAll(allByClassesName);
            }
        }
        return assignments;
    }

    public ApiResponse addAssignment(MultipartHttpServletRequest request) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        final String uploadingDirectoryProfile = "src/main/resources/profile";
        final String uploadingDirectoryAssignment = "src/main/resources/assignments";
        final String uploadingDirectorySubmission = "src/main/resources/submissions";

        Iterator<String> fileNames = request.getFileNames();
        String fileName = fileNames.next();

        MultipartFile file = request.getFile(fileName);
        assert file != null;
        long size = file.getSize();
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        Attachment attachment = new Attachment();
        attachment.setSize(size);
        attachment.setFileOriginalName(originalFilename);
        attachment.setContentType(contentType);
        String[] split = originalFilename.split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        attachment.setName(name); // fayl nomi saqlanadi

        Attachment save = attachmentRepository.save(attachment);



        Assignment newAssignment = new Assignment();
        newAssignment.setAttachment(save);
        newAssignment.setClasses(principal.getClasses().get(0));
        newAssignment.setUser(principal);
        assignmentRepository.save(newAssignment);
        // serverga yuklash

        if (attachment.isAssignment()) {
            Path path = Paths.get(uploadingDirectoryAssignment + "/" + name);
            Files.copy(file.getInputStream(), path);
        }
        if (attachment.isSubmission()) {
            Path path = Paths.get(uploadingDirectorySubmission + "/" + name);
            Files.copy(file.getInputStream(), path);
        }
        Path path = Paths.get(uploadingDirectoryProfile + "/" + name);
        Files.copy(file.getInputStream(), path);

        return new ApiResponse("saqlandi id : " + save.getId(), true);

    }
}
