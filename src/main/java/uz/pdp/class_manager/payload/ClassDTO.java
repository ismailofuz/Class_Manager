package uz.pdp.class_manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassDTO {
    private String name;
    private Integer teacherId;
    private List<Integer> studentId;
}
