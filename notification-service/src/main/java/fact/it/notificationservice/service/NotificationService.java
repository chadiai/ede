package fact.it.notificationservice.service;

import fact.it.notificationservice.dto.InventoryResponse;
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
            notification.setSkuCode("tube6in");
            notification.setQuantity(100);

            Notification notification1 = new Notification();
            notification1.setSkuCode("beam10ft");
            notification1.setQuantity(0);

            notificationRepository.save(notification);
            notificationRepository.save(notification1);
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {

        return notificationRepository.findBySkuCodeIn(skuCode).stream()
                .map(notification ->
                        InventoryResponse.builder()
                                .skuCode(notification.getSkuCode())
                                .isInStock(notification.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
