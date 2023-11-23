package com.turkeycrew.controller;

import com.turkeycrew.model.Restaurant;
import com.turkeycrew.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/register")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
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
