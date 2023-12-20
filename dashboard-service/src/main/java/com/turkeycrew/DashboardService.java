package com.turkeycrew;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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

    public ResponseEntity<String> getAllData() {
        List<LegacyData> legacyData = dashboardRepository.findAll();
        return ResponseEntity.ok(legacyData.toString());
    }

    /*
    Average Price by City/Area/Restaurant:
    Calculate the average price of meals in each city, area, or for each restaurant.
    Identify the areas or cities with the highest and lowest average prices.
     */
    public ResponseEntity<String> averagePriceByCityAreaRestaurant() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> averagePrices = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getCity, Collectors.averagingInt(LegacyData::getPrice)));

        return ResponseEntity.status(HttpStatus.OK).body(averagePrices.toString());
    }

    /*
    Average Rating by City/Area/Restaurant:
    Calculate the average ratings for each city, area, or restaurant.
    Identify the areas or restaurants with the highest and lowest average ratings.
     */
    public ResponseEntity<String> averageRatingByCityAreaRestaurant() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> averageRatings = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getCity, Collectors.averagingDouble(LegacyData::getAvg_rating)));

        return ResponseEntity.status(HttpStatus.OK).body(averageRatings.toString());
    }

    /*
    Distribution of Ratings:
    Visualize the distribution of ratings to understand the overall satisfaction level of customers.
     */
    public ResponseEntity<String> distributionOfRatings() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<Double, Long> ratingDistribution = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getAvg_rating, Collectors.counting()));

        return ResponseEntity.status(HttpStatus.OK).body(ratingDistribution.toString());
    }

    /*
    Most Popular Food Types:
    Identify the most popular food types based on the number of restaurants serving them.
     */
    public ResponseEntity<String> mostPopularFoodTypes() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Long> foodTypeDistribution = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getFood_type, Collectors.counting()));

        return ResponseEntity.status(HttpStatus.OK).body(foodTypeDistribution.toString());
    }

    /*
    Delivery Time Analysis:
    Analyze the average delivery times for different areas or cities.
    Identify outliers or areas where delivery times are unusually long.
     */
    public ResponseEntity<String> deliveryTimeAnalysis() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> averageDeliveryTimes = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getCity, Collectors.averagingInt(LegacyData::getDelivery_time)));

        return ResponseEntity.status(HttpStatus.OK).body(averageDeliveryTimes.toString());
    }

    /*
    Top Restaurants:
    Identify the top-rated restaurants based on average ratings and total ratings.
     */
    public ResponseEntity<String> topRestaurants() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> topRestaurantsByAverageRating = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getRestaurant, Collectors.averagingDouble(LegacyData::getAvg_rating)));

        Map<String, Integer> topRestaurantsByTotalRating = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getRestaurant, Collectors.summingInt(LegacyData::getTotal_rating)));

        return ResponseEntity.status(HttpStatus.OK).body(topRestaurantsByAverageRating.toString() + "\n" + topRestaurantsByTotalRating.toString());
    }

    /*
    Food Type Preferences:
    Analyze the popularity of different food types across cities or areas.
     */
    public ResponseEntity<String> foodTypePreferences() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Map<String, Long>> foodTypePreferencesByCity = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getCity, Collectors.groupingBy(LegacyData::getFood_type, Collectors.counting())));

        Map<String, Map<String, Long>> foodTypePreferencesByArea = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getArea, Collectors.groupingBy(LegacyData::getFood_type, Collectors.counting())));

        return ResponseEntity.status(HttpStatus.OK).body(foodTypePreferencesByCity.toString() + "\n" + foodTypePreferencesByArea.toString());
    }

    /*
    Average Total Ratings by City/Area/Restaurant:
    Explore the average total ratings received by restaurants in different cities or areas.
     */
    public ResponseEntity<String> averageTotalRatingsByCityAreaRestaurant() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> averageTotalRatingsByCity = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getCity, Collectors.averagingInt(LegacyData::getTotal_rating)));

        Map<String, Double> averageTotalRatingsByArea = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getArea, Collectors.averagingInt(LegacyData::getTotal_rating)));

        Map<String, Double> averageTotalRatingsByRestaurant = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getRestaurant, Collectors.averagingInt(LegacyData::getTotal_rating)));

        return ResponseEntity.status(HttpStatus.OK).body(averageTotalRatingsByCity.toString() + "\n" + averageTotalRatingsByArea.toString() + "\n" + averageTotalRatingsByRestaurant.toString());
    }

    /*
    Average Delivery Time by Food Type:
    Explore whether there is a correlation between the type of food and delivery time.
    */
    public ResponseEntity<String> averageDeliveryTimeByFoodType() {
        List<LegacyData> legacyData = dashboardRepository.findAll();

        if (legacyData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found");
        }

        Map<String, Double> averageDeliveryTimeByFoodType = legacyData.stream()
                .collect(Collectors.groupingBy(LegacyData::getFood_type, Collectors.averagingInt(LegacyData::getDelivery_time)));

        return ResponseEntity.status(HttpStatus.OK).body(averageDeliveryTimeByFoodType.toString());
    }

}
