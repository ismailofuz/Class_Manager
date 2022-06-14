package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Message;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.MessageRepository;
import uz.pdp.class_manager.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ApiResponse getMessage(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.map(message -> new ApiResponse("", true, message)).orElseGet(() -> new ApiResponse("Message not found", false));
    }

    public ApiResponse pinMessage(Message message) {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            if (user.getAuthorities().contains("STUDENT") && user.getSubject_name().equals(message.getUser().getSubject_name())) {
                message.setUser(user);
                user.setMessages(List.of(message));
                messageRepository.save(message);
            }
        }
        return new ApiResponse("Successfully send message", true);
    }
}
