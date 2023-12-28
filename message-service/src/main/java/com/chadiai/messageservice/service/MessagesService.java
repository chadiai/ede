package com.chadiai.messageservice.service;

import com.chadiai.messageservice.dto.MessagesResponse;
import com.chadiai.messageservice.model.Message;
import com.chadiai.messageservice.model.Messages;
import com.chadiai.messageservice.repository.MessagesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @PostConstruct
    public void loadData() {
        if(messagesRepository.count() <= 0){
            Message message = Message.builder()
                    .timestamp(new Date())
                    .content("Hello")
                    .senderId(1)
                    .seen(true)
                    .build();
            Message message2 = Message.builder()
                    .timestamp(new Date())
                    .content("Hi!")
                    .senderId(2)
                    .seen(true)
                    .build();
            List<Integer> participantsList = List.of(1,2);
            List<Message> messagesList = List.of(message,message2);
            Messages messages = Messages.builder()
                    .participants(participantsList)
                    .messages(messagesList)
                    .build();
            messagesRepository.save(messages);
        }
    }

    public List<MessagesResponse> getAllMessages() {
        List<Messages> messages = messagesRepository.findAll();
        return messages.stream().map(this::mapToMessagesResponse).toList();
    }


    private MessagesResponse mapToMessagesResponse(Messages messages) {
        return MessagesResponse.builder()
                .id(messages.getId())
                .messages(messages.getMessages())
                .participants(messages.getParticipants())
                .build();
    }
}
