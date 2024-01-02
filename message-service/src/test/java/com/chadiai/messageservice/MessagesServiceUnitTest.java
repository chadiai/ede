package com.chadiai.messageservice;

import com.chadiai.messageservice.dto.MessageRequest;
import com.chadiai.messageservice.dto.MessagesResponse;
import com.chadiai.messageservice.event.MessageSentEvent;
import com.chadiai.messageservice.model.Message;
import com.chadiai.messageservice.model.Messages;
import com.chadiai.messageservice.repository.MessagesRepository;
import com.chadiai.messageservice.service.MessagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MessagesServiceUnitTest {

    @InjectMocks
    private MessagesService messagesService;

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private KafkaTemplate<String, MessageSentEvent> kafkaTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        messagesService = new MessagesService(messagesRepository,kafkaTemplate);
    }

    @Test
    public void testCreateMessage() {
        // Arrange
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setTimestamp(new Date());
        messageRequest.setContent("Test message");
        messageRequest.setSenderId(1);
        messageRequest.setReceiverId(2);

        Messages existingMessages = Messages.builder()
                .participants(Arrays.asList(1, 2))
                .messages(new ArrayList<>())
                .build();

        when(messagesRepository.findByParticipantsIn(any())).thenReturn(Optional.of(existingMessages));

        // Act
        boolean result = messagesService.createMessage(1, messageRequest);

        // Assert
        verify(messagesRepository, times(1)).save(any());
        assertEquals(true, result);
    }

    @Test
    public void testGetMessagesByParticipants() {
        // Arrange
        Messages existingMessages = Messages.builder()
                .participants(Arrays.asList(1, 2))
                .messages(new ArrayList<>())
                .build();

        when(messagesRepository.findByParticipantsIn(any())).thenReturn(Optional.of(existingMessages));

        // Act
        Optional<Messages> result = messagesService.getMessagesByParticipants(1, 2);

        // Assert
        assertEquals(true, result.isPresent());
        assertEquals(existingMessages, result.get());
    }

    @Test
    public void testGetAllMessages() {
        // Arrange
        List<Message> messagesList = new ArrayList<>();
        Message message1 = Message.builder()
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
        messagesList.add(message1);
        messagesList.add(message2);

        Messages messages = Messages.builder()
                .participants(Arrays.asList(1, 2))
                .messages(messagesList)
                .build();

        when(messagesRepository.findAll()).thenReturn(Collections.singletonList(messages));

        // Act
        List<MessagesResponse> result = messagesService.getAllMessages();

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getMessages().size()); // Assuming two messages are returned
        assertEquals("Hello", result.get(0).getMessages().get(0).getContent());
        assertEquals("Hi!", result.get(0).getMessages().get(1).getContent());
    }

}
