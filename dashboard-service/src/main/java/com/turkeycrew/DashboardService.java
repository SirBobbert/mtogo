package com.turkeycrew;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DashboardService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DashboardService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void getDeliveries() {
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
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOrders() {
        String message = "Get order info";
        kafkaTemplate.send("GetAllOrdersTrigger", message);
    }

    @KafkaListener(topics = "GetAllOrders")
    public void getAllOrders(String message) {
        try {
            // Check if the message is JSON
            if (isJson(message)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(message);

                if (jsonNode.isArray()) {
                    for (JsonNode deliveryNode : jsonNode) {
                        // Do something with each entry in the array
                        System.out.println(deliveryNode.toString());
                        System.out.println();
                    }
                }
            } else {
                // Handle non-JSON messages
                System.out.println("Received non-JSON message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void getRestaurants() {
        String message = "Get restaurant info";
        kafkaTemplate.send("GetAllRestaurantsTrigger", message);
    }

    @KafkaListener(topics = "GetAllRestaurants")
    public void getAllRestaurants(String message) {
        try {
            // Check if the message is JSON
            if (isJson(message)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(message);

                if (jsonNode.isArray()) {
                    for (JsonNode restaurantNode : jsonNode) {
                        // Do something with each entry in the array
                        System.out.println(restaurantNode.toString());
                        System.out.println();
                    }
                }
            } else {
                // Handle non-JSON messages
                System.out.println("Received non-JSON message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFeedback() {
        String message = "Get feedback info";
        kafkaTemplate.send("GetAllFeedbackTrigger", message);
    }

    @KafkaListener(topics = "GetAllFeedback")
    public void getAllFeedback(String message) {
        try {
            // Check if the message is JSON
            if (isJson(message)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(message);

                if (jsonNode.isArray()) {
                    for (JsonNode restaurantNode : jsonNode) {
                        // Do something with each entry in the array
                        System.out.println(restaurantNode.toString());
                        System.out.println();
                    }
                }
            } else {
                // Handle non-JSON messages
                System.out.println("Received non-JSON message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if a string is valid JSON
    private boolean isJson(String str) {
        try {
            new ObjectMapper().readTree(str);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
