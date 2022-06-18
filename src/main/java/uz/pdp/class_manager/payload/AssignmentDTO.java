package uz.pdp.class_manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignmentDTO {
    private Integer classId;
    private Integer attachmentId;
    private Integer teacher_id;
}
