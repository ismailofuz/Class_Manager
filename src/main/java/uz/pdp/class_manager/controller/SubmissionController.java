package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.Submission;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.service.SubmissionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping
    public HttpEntity<List<Submission>> getSubmissions(@RequestBody Classes classes){
        List<Submission> submissions = submissionService.getSubmissions(classes);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getSubmission(@PathVariable Integer id){
        ApiResponse apiResponse = submissionService.getSubmission(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
