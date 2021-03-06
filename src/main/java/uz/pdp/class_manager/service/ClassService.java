package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.ClassDTO;
import uz.pdp.class_manager.repository.ClassRepository;
import uz.pdp.class_manager.repository.UserRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository classRepository;

    private final UserRepository userRepository;

    public ApiResponse classGet(Integer id) {
        Optional<Classes> optionalClasses = classRepository.findById(id);
        return optionalClasses.map(classes -> new ApiResponse("Found", true, classes)).orElseGet(() -> new ApiResponse("Class not found id = " + id, false));
    }

    public ApiResponse addClass(ClassDTO classDTO) {
        Optional<User> optionalUser = userRepository.findById(classDTO.getTeacherId());
        Optional<Classes> optional = classRepository.findByName(classDTO.getName());
        List<User> users = new ArrayList<>();
        users.add(optionalUser.get());
        if (optional.isPresent()) {
            return new ApiResponse("This class already exists", false);
        }
        if (optionalUser.isPresent()) {
            Classes newClass = new Classes();
            newClass.setName(classDTO.getName());
            newClass.setUsers(users);
            classRepository.save(newClass);
            optionalUser.get().setClasses(newClass);
            userRepository.save(optionalUser.get());
            return new ApiResponse("New class successfully created!", true);
        } else {
            return new ApiResponse("Teacher not found", false);
        }
    }

    public ApiResponse addStudent(ClassDTO classDTO, Integer id) {
        List<Integer> students = new ArrayList<>(classDTO.getStudentId());
        Optional<Classes> optionalClasses = classRepository.findById(id);
        List<User> users = new ArrayList<>();
        if (optionalClasses.isPresent()) {
            Classes classes = optionalClasses.get();
            for (Integer student : students) {
                Optional<User> optionalStudent = userRepository.findById(student);
                if (users.removeIf(user -> user.getId().equals(student))) {
                    return new ApiResponse("This student already exists", false);
                }
                users.add(optionalStudent.get());
                optionalClasses.get().setUsers(users);
                classes.setUsers(users);
                classRepository.save(classes);
                optionalStudent.get().setClasses(classes);
                userRepository.save(optionalStudent.get());
            }

            return new ApiResponse("Student(s) successfully added to class", true);
        }
        return new ApiResponse("Class not found", false);
    }
}

