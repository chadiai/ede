package com.chadiai.messageservice.service;

import com.chadiai.messageservice.dto.MessageRequest;
import com.chadiai.messageservice.dto.MessagesResponse;
import com.chadiai.messageservice.event.MessageSentEvent;
import com.chadiai.messageservice.model.Message;
import com.chadiai.messageservice.model.Messages;
import com.chadiai.messageservice.repository.MessagesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessagesService {
    private final MessagesRepository messagesRepository;
    private final KafkaTemplate<String, MessageSentEvent> kafkaTemplate;

    @PostConstruct
    public void loadData() {
        if(messagesRepository.count() <= 0){
            Message message = Message.builder()
                    .timestamp(new Date())
                    .content("Hello")
                    .senderId(1)
                    .receiverId(2)
                    .seen(true)
                    .build();
            Message message2 = Message.builder()
                    .timestamp(new Date())
                    .content("Hi!")
                    .senderId(2)
                    .receiverId(1)
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

    public boolean createMessage(int sender,MessageRequest newMessage) {
        int senderId = newMessage.getSenderId();
        if (senderId != sender) return false;
        int receiverId = newMessage.getReceiverId();

        Optional<Messages> existingMessagesOptional = getMessagesByParticipants(senderId, receiverId);

        if (existingMessagesOptional.isPresent()) {
            // Add the new message to the existing Messages
            Messages existingMessages = existingMessagesOptional.get();
            Message message = mapToMessage(newMessage);
            existingMessages.getMessages().add(message);
            saveMessages(existingMessages,sender);
        } else {
            // Create a new Messages and add the new message to it
            Message message = mapToMessage(newMessage);
            Messages newMessages = Messages.builder()
                    .participants(Arrays.asList(senderId, receiverId))
                    .messages(Collections.singletonList(message))
                    .build();
            saveMessages(newMessages,sender);
        }
        return true;
    }

    public void saveMessages(Messages messages, int senderId) {
        messagesRepository.save(messages);
        int receiverId = messages.getParticipants().stream()
                .filter(p -> p != senderId)
                .findFirst()
                .orElse(-1);
        kafkaTemplate.send("sentMessage", new MessageSentEvent(senderId,receiverId));
    }

    private Message mapToMessage(MessageRequest message) {
        return Message.builder()
                .timestamp(message.getTimestamp())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .build();
    }
    private MessagesResponse mapToMessagesResponse(Messages messages) {
        return MessagesResponse.builder()
                .id(messages.getId())
                .messages(messages.getMessages())
                .participants(messages.getParticipants())
                .build();
    }

    public Optional<Messages> getMessagesByParticipants(int senderId, int receiverId) {
        Collection<List<Integer>> combinations = Arrays.asList(
                Arrays.asList(senderId, receiverId),
                Arrays.asList(receiverId, senderId)
        );
        return messagesRepository.findByParticipantsIn(combinations);
    }
}
