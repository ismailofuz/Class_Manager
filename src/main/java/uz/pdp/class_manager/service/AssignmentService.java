package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Assignment;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.AssignmentRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    public ApiResponse getAssignment(Integer id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);
        return optionalAssignment.map(assignment -> new ApiResponse("", true, assignment)).orElseGet(() -> new ApiResponse("This assignment not found", false));
    }
}
