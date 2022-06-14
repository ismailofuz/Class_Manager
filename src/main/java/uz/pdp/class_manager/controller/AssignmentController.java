package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.class_manager.entity.Assignment;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.AssignmentRepository;
import uz.pdp.class_manager.service.AssignmentService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/assignment")
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    private final AssignmentService assignmentService;
    @GetMapping
    public HttpEntity<List<Assignment>> getAssignments(){
        List<Assignment> assignments = assignmentRepository.findAll();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getAssignment(@PathVariable Integer id){
        ApiResponse apiResponse = assignmentService.getAssignment(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
