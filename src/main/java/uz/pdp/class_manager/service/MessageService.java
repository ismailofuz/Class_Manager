package uz.pdp.class_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.class_manager.entity.Classes;
import uz.pdp.class_manager.entity.Message;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.entity.enums.RoleEnum;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.repository.MessageRepository;
import uz.pdp.class_manager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public ApiResponse getMessage(Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.map(message -> new ApiResponse("", true, message)).orElseGet(() -> new ApiResponse("Message not found", false));
    }

    public ApiResponse pinMessage(Message message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        message.setUser(principal);
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        principal.setMessages(messages);
        messageRepository.save(message);
//        List<User> all = userRepository.findAll();
//        for (User user : all) {
//            if (user.getRole().equals(RoleEnum.STUDENT)) {
//                for (Object o : user.getSubject_names().toArray()) {
//                    if (o.equals(message.getUser().getSubject_names())) {
//                        List<Message> messages = new ArrayList<>();
//                        messages.add(message);
//                        user.setMessages(messages);
//                        messageRepository.save(message);
//                    }
//                }
//            }
//        }
        return new ApiResponse("Successfully send message", true);
    }

    public List<Message> getMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Message> messages = messageRepository.findAll();
        List<Message> getMessages = new ArrayList<>();
        if (user.getRole().equals(RoleEnum.STUDENT)){
            for (Classes subject_name : user.getClasses()) {
                for (Message message : messages) {
                    if (subject_name.equals(message.getUser().getClasses().get(0))) {
                        getMessages.add(message);
                    }
                }
            }
        }
        return getMessages;
    }
}
