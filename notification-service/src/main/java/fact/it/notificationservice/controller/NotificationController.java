package fact.it.notificationservice.controller;

import fact.it.notificationservice.dto.NotificationResponse;
import fact.it.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> isUnread
    (@RequestParam boolean bool) {
        return notificationService.isUnread(bool);
    }
}