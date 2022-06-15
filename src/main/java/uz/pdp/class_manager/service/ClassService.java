package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.ClassRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ApiResponse classGet(Integer id) {
        Optional<Classes> optionalClasses = classRepository.findById(id);
        return optionalClasses.map(classes -> new ApiResponse("Found", true, classes)).orElseGet(() -> new ApiResponse("Class not found id = " + id, false));
    }

    public ApiResponse addClass(Classes classes) {
        Optional<Classes> optional = classRepository.findByName(classes.getName());
        if (optional.isPresent()) {
            return new ApiResponse("This class already exists", false);
        }
        Classes newClass = new Classes();
        newClass.setName(classes.getName());
        classRepository.save(newClass);
        return new ApiResponse("New class successfully created!", true);
    }
}
