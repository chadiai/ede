package fact.it.messageservice.controller;

import fact.it.messageservice.dto.MessageResponse;
import fact.it.messageservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<fact.it.messageservice.dto.MessageResponse> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/seen/{isSeen}")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponse> getAllSeenMessages(@PathVariable boolean isSeen) {
        return messageService.getAllSeen(isSeen);
    }

}