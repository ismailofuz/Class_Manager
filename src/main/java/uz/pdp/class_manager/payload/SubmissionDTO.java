package uz.pdp.class_manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmissionDTO {
    private Integer classId;
    private Integer studentId;
    private Integer attachmentId;
}
