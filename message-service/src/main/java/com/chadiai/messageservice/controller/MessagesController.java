package com.chadiai.messageservice.controller;

import com.chadiai.messageservice.client.UserServiceClient;
import com.chadiai.messageservice.dto.MessagesResponse;
import com.chadiai.messageservice.dto.UserResponse;
import com.chadiai.messageservice.model.Messages;
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
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MessagesResponse> getAllMessages() {
        return messagesService.getAllMessages();
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


    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable int id) {
        return userServiceClient.fetchUser(id);
    }

}
