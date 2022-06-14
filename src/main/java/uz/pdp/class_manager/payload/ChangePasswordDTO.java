package uz.pdp.class_manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO {
    @NotBlank(message = "Please enter old passowrd")
    private String oldPassword;

    @Size(min = 6, message = "Please enter code in valid type")
    private String newPassword;

    @Size(min = 6, message = "Please enter code in valid type")
    private String confirmNewPassword;
}
