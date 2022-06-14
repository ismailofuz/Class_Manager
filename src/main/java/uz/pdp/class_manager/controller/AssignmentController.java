package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.class_manager.repository.AssignmentRepository;

@RequiredArgsConstructor
@RequestMapping("/api/assignment")
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    @GetMapping
    public HttpEntity<?> getAssignments(){
        return null;
    }

}
