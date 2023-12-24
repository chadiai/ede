package fact.it.notificationservice.service;

import fact.it.notificationservice.dto.NotificationResponse;
import fact.it.notificationservice.model.Notification;
import fact.it.notificationservice.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @PostConstruct
    public void loadData() {
        if(notificationRepository.count() <= 0){
            Notification notification = new Notification();
            notification.setUserId("1");
            notification.setUnread(false);
            notification.setMessage("message1");

            Notification notification1 = new Notification();
            notification1.setUserId("2");
            notification1.setUnread(true);
            notification1.setMessage("message2");

            notificationRepository.save(notification);
            notificationRepository.save(notification1);
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> isUnread(boolean bool) {
        return notificationRepository.findByUnread(bool).stream()
                .map(notification ->
                        NotificationResponse.builder()
                                .message(notification.getMessage())
                                .userId(notification.getUserId())
                                .unread(notification.isUnread())
                                .build()
                ).toList();
    }
}
