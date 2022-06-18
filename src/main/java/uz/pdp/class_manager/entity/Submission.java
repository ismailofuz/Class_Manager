package uz.pdp.class_manager.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Transactional
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Attachment attachment;

    @CreatedDate
    private Timestamp timestamp;

    @ManyToOne
    private Classes courseWork;

    @ManyToOne
    private User student;

    // teacher tomonidan qo'yiladi
    private Integer grade;
}
