package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.class_manager.entity.Assignment;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.AssignmentDTO;
import uz.pdp.class_manager.repository.AssignmentRepository;
import uz.pdp.class_manager.service.AssignmentService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    private final AssignmentService assignmentService;
    @GetMapping("/getAll")
    public HttpEntity<List<Assignment>> getAssignments(Classes classes){
        List<Assignment> assignments = assignmentService.getAssignments(classes);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public HttpEntity<ApiResponse> getAssignment(@PathVariable Integer id){
        ApiResponse apiResponse = assignmentService.getAssignment(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PostMapping("/add")
    public HttpEntity<?> addAssignment(MultipartHttpServletRequest request) throws IOException {
        ApiResponse apiResponse = assignmentService.addAssignment(request);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }
}
