package com.portfolio.controller;

import com.portfolio.model.Message;
import com.portfolio.repository.MessageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // POST /api/messages  -> submit the contact form
    @PostMapping
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) {
        Message saved = messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/messages   -> (admin) list submitted messages
    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    // PATCH /api/messages/{id}/read -> (admin) mark as read
    @PatchMapping("/{id}/read")
    public ResponseEntity<Message> markRead(@PathVariable Long id) {
        return messageRepository.findById(id).map(m -> {
            m.setRead(true);
            return ResponseEntity.ok(messageRepository.save(m));
        }).orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation() {
        return ResponseEntity.badRequest().body(Map.of("error", "Please fill in all required fields correctly."));
    }
}
