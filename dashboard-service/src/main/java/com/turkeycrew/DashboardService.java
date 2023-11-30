package com.turkeycrew;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DashboardService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void getDelivery() {
        String message = "Get delivery info";
        kafkaTemplate.send("GetAllDeliveriesTrigger", message);
    }

    @KafkaListener(topics = "GetAllDeliveries")
    public void getAllDeliveries(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            if (jsonNode.isArray()) {
                for (JsonNode deliveryNode : jsonNode) {
                    // Do something with each entry in the array
                    System.out.println(deliveryNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
