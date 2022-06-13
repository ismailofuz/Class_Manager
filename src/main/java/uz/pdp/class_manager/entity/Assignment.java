package uz.pdp.class_manager.entity;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Transactional
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Attachment attachment;

    @ManyToOne
    private User user;



}
