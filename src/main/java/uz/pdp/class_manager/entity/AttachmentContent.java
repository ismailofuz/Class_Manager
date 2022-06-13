package uz.pdp.class_manager.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttachmentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private byte[] bytes; //asosiy content

    @OneToOne // foreign key bo'ladi unga bosganda uni contentini ham op  keladi
    private Attachment attachment;

}
