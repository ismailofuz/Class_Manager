package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.class_manager.entity.User;

import uz.pdp.class_manager.entity.enums.RoleEnum;
import uz.pdp.class_manager.payload.*;

import uz.pdp.class_manager.repository.UserRepository;
import uz.pdp.class_manager.security.Encoder;
import uz.pdp.class_manager.security.JwtProvider;
import uz.pdp.class_manager.service.AuthService;


import javax.naming.NameNotFoundException;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/{id}")
    public HttpEntity<?> one(@PathVariable Integer id) {
        ApiResponse apiResponse = authService.one(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse.getObject());
    }


    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        ApiResponse apiResponse;

        try {
            Authentication authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

            User principal = (User) authentication.getPrincipal();

            String token = jwtProvider.generateToken(dto.getUsername());

            apiResponse = new ApiResponse("Your token :" + token, true, principal);


        } catch (AuthenticationException e) {

            apiResponse = new ApiResponse("something went wrong :" + e.getMessage(), false);

        }

        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/editProfile")
    public HttpEntity<?> editProfile(@RequestBody UserUpdateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User editUser = (User) authentication.getPrincipal();
        ApiResponse apiResponse = authService.editProfile(dto, editUser);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PostMapping("/changePassword")
    public HttpEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        ApiResponse apiResponse = authService.changePassword(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.CONFLICT).body(apiResponse);
    }

    @PostMapping("/checkOldPassword")
    public HttpEntity<Boolean> checkOldPassword(String dto) {
        ApiResponse apiResponse = authService.checkOldPassword(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse.isSuccess());
    }

    @GetMapping("/getTeachers")
    public HttpEntity<List<User>> getUsers() {
        List<User> teachers = authService.getTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/getStudents")
    public HttpEntity<List<User>> getStudents() {
        List<User> students = authService.getStudents();
        return ResponseEntity.ok(students);
    }


    @Transactional
    @PostMapping("/register")
    public HttpEntity<?> register(@Valid @RequestBody RegisterDTO dto) throws NameNotFoundException {
        User user = new User();
        // username bilan olishdan avval unique bo'lishini aniqlab olishimiza kerak
        boolean email = userRepository.existsByEmail(dto.getEmail());
        boolean username = userRepository.existsByUsername(dto.getUsername());
        if (username || email) {
            ApiResponse apiResponse = new ApiResponse("This email or username already exists, please try again", false);
            return ResponseEntity.badRequest().body(apiResponse);
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());  // default holatda bo'ladi 1 id li
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());

        user.setPassword(passwordEncoder.encode(dto.getPassword())); // password doim encode holda saqlanadi
        User saved_user = userRepository.save(user);

        ApiResponse apiResponse = new ApiResponse("Registered successfully please turn it to login page", true, saved_user);


        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }


    // xatolik bo'lsa requiredlarni ko'rsatadi
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
