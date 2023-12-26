package fact.it.messageservice.service;

import fact.it.messageservice.dto.MessageResponse;
import fact.it.messageservice.model.Message;
import fact.it.messageservice.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @PostConstruct
    public void loadData() {
        if(messageRepository.count() <= 0){
            Message message = Message.builder()
                    .content("Hello")
                    .receiverId(1L)
                    .senderId(2L)
                    .seen(false)
                    .build();
            Message message2 = Message.builder()
                    .content("Hello!")
                    .receiverId(2L)
                    .senderId(1L)
                    .seen(false)
                    .build();
            messageRepository.save(message);
            messageRepository.save(message2);
        }
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getAllSeen(boolean bool) {
        return messageRepository.findAllBySeen(bool).stream()
                .map(this::mapToMessageResponse).toList();
    }

    public List<fact.it.messageservice.dto.MessageResponse> getAllMessages() {
        List<Message> appointments = messageRepository.findAll();
        return appointments.stream().map(this::mapToMessageResponse).toList();
    }


    private fact.it.messageservice.dto.MessageResponse mapToMessageResponse(Message message) {
        return fact.it.messageservice.dto.MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .receiverId(message.getReceiverId())
                .senderId(message.getSenderId())
                .seen(message.isSeen())
                .build();
    }
}
