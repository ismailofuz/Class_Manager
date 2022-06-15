package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.ClassRepository;
import uz.pdp.class_manager.service.ClassService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/class")
public class ClassController {

    private final ClassService classService;

    private final ClassRepository classRepository;

    @GetMapping("/getClass/{id}")
    public HttpEntity<?> getClass(@PathVariable Integer id) {
        ApiResponse apiResponse = classService.classGet(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse.getObject());
    }

    @GetMapping("/getAllClass")
    public HttpEntity<List<Classes>> getClasses() {
        return ResponseEntity.ok(classRepository.findAll());
    }

    @PostMapping("/addClass")
    public HttpEntity<ApiResponse> addClass(@Valid @RequestBody Classes classes){
        ApiResponse apiResponse = classService.addClass(classes);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
