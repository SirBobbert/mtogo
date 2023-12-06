package com.turkeycrew.service;

import com.turkeycrew.model.Restaurant;
import com.turkeycrew.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Validator validator;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public ResponseEntity<?> getRestaurantById(Integer restaurantId) {

        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant ID is required.");
        }
        return new ResponseEntity<>(restaurantRepository.findById(restaurantId), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllRestaurants() {
        try {
            // Fetch all restaurants from the repository
            List<Restaurant> restaurants = restaurantRepository.findAll();

            if (restaurants.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(restaurants, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching restaurants");
        }
    }

    public ResponseEntity<String> addRestaurant(Restaurant restaurant) {

        if (restaurantRepository.existsByName(restaurant.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the name " + restaurant.getName() + " already exists");
        }

        if (restaurantRepository.existsByAddress(restaurant.getAddress())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the address " + restaurant.getAddress() + " already exists");
        }

        if (restaurantRepository.existsByEmail(restaurant.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the email " + restaurant.getEmail() + " already exists");
        }

        if (restaurantRepository.existsByPhoneNumber(restaurant.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the phone number " + restaurant.getPhoneNumber() + " already exists");
        }

        if (restaurantRepository.existsByWebsite(restaurant.getWebsite())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the website " + restaurant.getWebsite() + " already exists");
        }

        restaurantRepository.save(restaurant);
        return ResponseEntity.ok("Restaurant added successfully");
    }


    public ResponseEntity<?> updateRestaurant(Integer restaurantId, Restaurant restaurant) {

        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant ID is required.");
        }

        if (!restaurant.getName().equals(restaurant.getName()) && restaurantRepository.existsByName(restaurant.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the name " + restaurant.getName() + " already exists");
        }

        if (!restaurant.getAddress().equals(restaurant.getAddress()) && restaurantRepository.existsByAddress(restaurant.getAddress())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the address " + restaurant.getAddress() + " already exists");
        }

        if (!restaurant.getEmail().equals(restaurant.getEmail()) && restaurantRepository.existsByEmail(restaurant.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the email " + restaurant.getEmail() + " already exists");
        }

        if (!restaurant.getPhoneNumber().equals(restaurant.getPhoneNumber()) && restaurantRepository.existsByPhoneNumber(restaurant.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the phone number " + restaurant.getPhoneNumber() + " already exists");
        }

        if (!restaurant.getWebsite().equals(restaurant.getWebsite()) && restaurantRepository.existsByWebsite(restaurant.getWebsite())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the website " + restaurant.getWebsite() + " already exists");
        }

        restaurant.setRestaurantId(restaurantId);
        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant updated successfully");
    }

    @KafkaListener(topics = "GetAllRestaurantsTrigger", groupId = "order-group")
    public void getAllDeliveriesTrigger(String message) {
        System.out.println("Received message from Kafka:");
        System.out.println(message);
        kafkaTemplate.send("GetAllRestaurants", restaurantRepository.findAll());
    }
}
