package com.turkeycrew;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private final KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "updateOrderByDeliveryId", groupId = "notification_id")
    public void sendNotification() {
        // TODO: send notification on kafka triggers
        kafkaTemplate.send("notificationTest", "Nice");
    }

    @KafkaListener(topics = "notificationTest", groupId = "notification_id")
    public void sendNotification2(String message) {
        System.out.println(message);
    }
}
