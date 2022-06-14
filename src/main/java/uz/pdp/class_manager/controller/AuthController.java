package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.class_manager.entity.User;

import uz.pdp.class_manager.payload.ApiResponse;

import uz.pdp.class_manager.payload.LoginDTO;

import uz.pdp.class_manager.payload.RegisterDTO;
import uz.pdp.class_manager.payload.UserUpdateDto;
import uz.pdp.class_manager.repository.UserRepository;
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

    @PutMapping("/{id}")
    public HttpEntity<?> editProfile(@PathVariable Integer id, @RequestBody UserUpdateDto dto) {
        ApiResponse apiResponse = authService.editProfile(id, dto);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @GetMapping("/getTeachers")
    public HttpEntity<List<User>> getUsers(){
        List<User> users = authService.getUsers();
        return ResponseEntity.ok(users);
    }

//    @PostMapping("/register")
//    public HttpEntity<?> register(@Valid @RequestBody RegisterDTO dto) throws NameNotFoundException {
//        User user = new User();
//        Optional<UserRole> optionalUserRole = roleRepository.findById(UUID.fromString("1565e035-4f3e-487f-b69f-5370bc2f0639"));
//
//        if (optionalUserRole.isPresent()) {
//
//            // username bilan olishdan avval unique bo'lishini aniqlab olishimiza kerak
//
//            Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
//            if(optionalUser.isPresent()){
//                ApiResponse apiResponse = new ApiResponse("This email already exists, please try again",false);
//
//                return ResponseEntity.badRequest().body(apiResponse);
//
//            }
//
//            user.setUserRole(optionalUserRole.get());  // default holatda bo'ladi 1 id li
//            user.setEmail(dto.getEmail());
//            user.setUsername(dto.getUsername());
//
//            user.setPassword(passwordEncoder.encode(dto.getPassword())); // password doim encode holda saqlanadi
//            User saved_user = userRepository.save(user);
//
//            ApiResponse apiResponse = new ApiResponse("Registered successfully please turn it to login page", true, saved_user);
//
//
//            return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
//        } else {
//            throw new NameNotFoundException("with this name role not found");
//        }
//    }


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
