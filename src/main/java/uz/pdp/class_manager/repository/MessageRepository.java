package uz.pdp.class_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.class_manager.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
