package com.turkeycrew.controller;

import com.turkeycrew.model.Restaurant;
import com.turkeycrew.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3000") // Replace with the actual origin of your React app
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/register")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/find/{restaurantId}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Integer restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    @PutMapping("/update/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Integer restaurantId, @RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurant(restaurantId, restaurant);
    }


}
