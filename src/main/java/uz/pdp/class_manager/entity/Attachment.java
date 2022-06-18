package uz.pdp.class_manager.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileOriginalName; // pdp.jpg

    private long size; // 2048000

    private String contentType; // image/jpg kabilar
// databasedan rawni olganda uni 100MB ni ham op keladi
//    private byte[] bytes; // asosiy content

    @Column(unique = true) // doim unique bo'ladi bu
    // serverda turgan faylning nomi
    private String name;

    private boolean isAssignment = false;

    private boolean isSubmission = false;

}
