package uz.pdp.class_manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {

    private String username;

    @Column(unique = true)
    private String email;

    private Integer attachmentId;

    private String newPassword;

    private String confirmNewPassword;

}
