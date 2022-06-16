package uz.pdp.class_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Please enter class name!")
    private String name;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private User teacher;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> student;
}
