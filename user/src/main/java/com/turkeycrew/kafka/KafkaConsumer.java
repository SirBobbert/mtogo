package com.turkeycrew.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

//    Listens for messages on the desired topic and prints them to the console
    @KafkaListener(topics = "your-topic-name", groupId = "your-consumer-group")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received Message: " + record.value());
    }
}
