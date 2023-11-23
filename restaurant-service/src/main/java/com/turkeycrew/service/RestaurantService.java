package com.turkeycrew.service;

import com.turkeycrew.model.Restaurant;
import com.turkeycrew.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final Validator validator;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, Validator validator) {
        this.restaurantRepository = restaurantRepository;
        this.validator = validator;
    }

    public ResponseEntity<String> addRestaurant(Restaurant restaurant) {
        // Validate the Restaurant object using the configured validator
        BindingResult bindingResult = new BeanPropertyBindingResult(restaurant, "restaurant");
        validator.validate(restaurant, bindingResult);

        if (bindingResult.hasErrors()) {
            // Return validation errors to the client
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors: " + bindingResult.toString());
        }

        // Check for duplicate name
        if (restaurantRepository.existsByName(restaurant.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the name " + restaurant.getName() + " already exists");
        }

        // Check for duplicate address
        if (restaurantRepository.existsByAddress(restaurant.getAddress())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the address " + restaurant.getAddress() + " already exists");
        }

        // Check for duplicate email
        if (restaurantRepository.existsByEmail(restaurant.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the email " + restaurant.getEmail() + " already exists");
        }

        // Check for duplicate phone
        if (restaurantRepository.existsByPhoneNumber(restaurant.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the phone number " + restaurant.getPhoneNumber() + " already exists");
        }

        // Check for duplicate website
        if (restaurantRepository.existsByWebsite(restaurant.getWebsite())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the website " + restaurant.getWebsite() + " already exists");
        }

        // If no duplicates found, save the restaurant
        restaurantRepository.save(restaurant);

        return ResponseEntity.ok("Restaurant added successfully");
    }

    public ResponseEntity<?> getRestaurantById(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

        if (restaurantOptional.isPresent()) {
            return ResponseEntity.ok(restaurantOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant with ID " + restaurantId + " not found");
        }
    }

    public ResponseEntity<?> updateRestaurant(Integer restaurantId, Restaurant restaurant) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurantToUpdate = restaurantOptional.get();
            restaurant.setRestaurant_id(restaurantId);

            // Validate the Restaurant object using the configured validator
            BindingResult bindingResult = new BeanPropertyBindingResult(restaurant, "restaurant");
            validator.validate(restaurant, bindingResult);

            if (bindingResult.hasErrors()) {
                // Return validation errors to the client
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors: " + bindingResult.toString());
            }

            // Check for duplicate name only if it has been changed
            if (!restaurant.getName().equals(restaurantToUpdate.getName()) && restaurantRepository.existsByName(restaurant.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the name " + restaurant.getName() + " already exists");
            }

            // Check for duplicate address only if it has been changed
            if (!restaurant.getAddress().equals(restaurantToUpdate.getAddress()) && restaurantRepository.existsByAddress(restaurant.getAddress())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the address " + restaurant.getAddress() + " already exists");
            }

            // Check for duplicate email only if it has been changed
            if (!restaurant.getEmail().equals(restaurantToUpdate.getEmail()) && restaurantRepository.existsByEmail(restaurant.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the email " + restaurant.getEmail() + " already exists");
            }

            // Check for duplicate phone only if it has been changed
            if (!restaurant.getPhoneNumber().equals(restaurantToUpdate.getPhoneNumber()) && restaurantRepository.existsByPhoneNumber(restaurant.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the phone number " + restaurant.getPhoneNumber() + " already exists");
            }

            // Check for duplicate website only if it has been changed
            if (!restaurant.getWebsite().equals(restaurantToUpdate.getWebsite()) && restaurantRepository.existsByWebsite(restaurant.getWebsite())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A restaurant with the website " + restaurant.getWebsite() + " already exists");
            }

            // If no duplicates found or no changes in the relevant fields, update the restaurant
            restaurantRepository.save(restaurant);

            return ResponseEntity.ok("Restaurant updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found with ID: " + restaurantId);
        }
    }

}
