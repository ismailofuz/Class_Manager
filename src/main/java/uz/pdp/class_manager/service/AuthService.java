package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Attachment;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.entity.enums.RoleEnum;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.ChangePasswordDTO;
import uz.pdp.class_manager.payload.UserUpdateDto;
import uz.pdp.class_manager.repository.AttachmentRepository;
import uz.pdp.class_manager.repository.UserRepository;
import uz.pdp.class_manager.security.Encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException("Sorry we couldn't found user with " + username + " 😔⁉️");
        }
    }

    public ApiResponse editProfile(UserUpdateDto dto, User editUser) {
        Optional<User> optionalUser = userRepository.findByUsername(dto.getUsername());
        Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
        if (optionalUser.isPresent() || userOptional.isPresent()) {
            return new ApiResponse("This username or email already exists", false);
        }
        Optional<Attachment> optionalAttachment = null;
        if (dto.getAttachmentId() != null) {
            optionalAttachment = attachmentRepository.findById(dto.getAttachmentId());
        }
//        Optional<User> user = userRepository.findById(id);

//        User editUser = user.get();
        if (!dto.getFirstName().equals("null")) {
            editUser.setFirstName(dto.getFirstName());
        }
        if (!dto.getLastName().equals("null")) {
            editUser.setLastName(dto.getLastName());
        }
        if (dto.getAttachmentId() != null) {
            editUser.setAttachment(optionalAttachment.get());
        }
        if (!dto.getEmail().equals("null")) {
            editUser.setEmail(dto.getEmail());
        }
//        editUser.setPassword(encoder.encode(dto.getNewPassword()));
        if (!dto.getUsername().equals("null")) {
            editUser.setUsername(dto.getUsername());
        }
        userRepository.save(editUser);
        return new ApiResponse("User successfully edited", true, editUser);
    }

    public List<User> getTeachers() {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().equals(RoleEnum.TEACHER)) {
                users.add(user);
            }
        }
        return users;
    }

    public List<User> getStudents() {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            if (user.getRole().equals(RoleEnum.STUDENT)) {
                users.add(user);
            }
        }
        return users;
    }

    public ApiResponse changePassword(ChangePasswordDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        if (checkOldPassword(dto.getOldPassword()).isSuccess()) {
            if (dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                principal.setPassword(dto.getNewPassword());
                return new ApiResponse("Password successfully edited", true);
            }
            return new ApiResponse("Password not match", false);
        }
        return new ApiResponse("old password incorrect", false);
    }

    public ApiResponse checkOldPassword(String dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        String encode = encoder.encode(dto);
        if (encode.equals(principal.getPassword())) {
            return new ApiResponse("Success", true);
        }
        return new ApiResponse("Old password incorrect", false);
    }

    public ApiResponse one(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.map(user -> new ApiResponse("Mana", true, user)).orElseGet(() -> new ApiResponse("Not found", false));
    }
}
