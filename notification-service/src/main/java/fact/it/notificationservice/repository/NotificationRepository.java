package fact.it.notificationservice.repository;

import fact.it.notificationservice.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUnread(boolean unread);
}
