package com.chadiai.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "messageSent",groupId = "notificationId")
    public void handleNotification(MessageSentEvent messageSentEvent) {
        log.info("UserID:" + messageSentEvent.getReceiverId() + " has received a new message");
    }

}
