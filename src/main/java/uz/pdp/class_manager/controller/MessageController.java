package uz.pdp.class_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.class_manager.entity.Message;
import uz.pdp.class_manager.entity.User;
import uz.pdp.class_manager.payload.ApiResponse;
import uz.pdp.class_manager.payload.MessageDTO;
import uz.pdp.class_manager.repository.MessageRepository;
import uz.pdp.class_manager.service.MessageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageRepository messageRepository;

    private final MessageService messageService;

    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        ApiResponse apiResponse = messageService.getMessage(id);
        return ResponseEntity.status(apiResponse.isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body((Message) apiResponse.getObject());
    }

    @PostMapping("/pin")
    public HttpEntity<?> addMessage(@RequestBody Message message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        ApiResponse apiResponse = messageService.pinMessage(message);
        return ResponseEntity.ok().body(apiResponse);
    }
}
