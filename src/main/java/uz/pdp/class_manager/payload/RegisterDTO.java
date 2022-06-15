package uz.pdp.class_manager.payload;

import lombok.*;
import uz.pdp.class_manager.entity.enums.RoleEnum;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegisterDTO {

    @NotBlank(message = "Enter your first name!")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Enter your last name!")
    @Column(nullable = false)
    private String lastName;

    @Size(min = 3,message = "Your username should be more than 3 characters")
    private String username;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Size(min = 6,message = "Please enter code in valid type")
    private String password;

    @Size(min = 6,message = "Please enter code in valid type")
    private String confirmPassword;
}
