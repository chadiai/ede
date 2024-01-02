package com.chadiai.messageservice.controller;
import com.chadiai.messageservice.dto.MessageRequest;
import com.chadiai.messageservice.dto.MessagesResponse;
import com.chadiai.messageservice.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MessagesResponse> getAllMessages() {
        return messagesService.getAllMessages();
    }

    @PostMapping("/{senderId}/send")
    public ResponseEntity<String> createMessage(@PathVariable int senderId, @RequestBody MessageRequest newMessage) {
        boolean response = messagesService.createMessage(senderId, newMessage);
        if (!response) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request: Wrong senderId");
        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
    }

    @GetMapping("/auth/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getAllAuthMessages(@PathVariable int id) {
        List<MessagesResponse> allMessages = messagesService.getAllMessages();

        List<MessagesResponse> filteredMessages = allMessages.stream()
                .filter(message -> message.getParticipants().contains(id))
                .toList();

        return filteredMessages.stream()
                .map(MessagesResponse::getId)
                .collect(Collectors.joining(";"));
    }

    @GetMapping("/m/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessagesResponse> getAllMessagesById(@PathVariable String id) {
        List<MessagesResponse> allMessages = messagesService.getAllMessages();

        Optional<MessagesResponse> messageOptional = allMessages.stream()
                .filter(message -> id.equals(message.getId()))
                .findFirst();

        return messageOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
