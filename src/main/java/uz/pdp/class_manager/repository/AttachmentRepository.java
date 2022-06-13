package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {

}
