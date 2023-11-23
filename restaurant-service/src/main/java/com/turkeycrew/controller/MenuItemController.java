package com.turkeycrew.controller;

import com.turkeycrew.model.MenuItem;
import com.turkeycrew.model.Restaurant;
import com.turkeycrew.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/find/{restaurantId}")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable Integer restaurantId) {
        return menuItemService.getRestaurantMenuById(restaurantId);
    }

    @PostMapping("/addMenuItem/{restaurantId}")
    public ResponseEntity<?> addMenuItem(@PathVariable Integer restaurantId, @RequestBody MenuItem menuItem) {
        System.out.println("Received request with menuItem: " + menuItem);

        // Check if the restaurant is not null before accessing it
        if (menuItem.getRestaurant() != null) {
            // Handle the case where the restaurant is provided in the request
            return ResponseEntity.badRequest().body("Restaurant information should not be included in the request body.");
        }

        if (menuItem.getMenu_item_id() != null) {
            // Handle the case where the menu item ID is provided in the request
            return ResponseEntity.badRequest().body("Menu item ID should not be included in the request body.");
        }

        if (menuItem.getName() == null || menuItem.getName().isEmpty()) {
            // Handle the case where the menu item name is not provided in the request
            return ResponseEntity.badRequest().body("Menu item name is required.");
        }

        if (menuItem.getDescription() == null || menuItem.getDescription().isEmpty()) {
            // Handle the case where the menu item description is not provided in the request
            return ResponseEntity.badRequest().body("Menu item description is required.");
        }

        if (menuItem.getPrice() == null || menuItem.getPrice() <= 0) {
            // Handle the case where the menu item price is not provided in the request
            return ResponseEntity.badRequest().body("Menu item price is required.");
        }

        // Create a new Restaurant object and set its ID
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);

        // Set the restaurant in the menuItem
        menuItem.setRestaurant(restaurant);

        return menuItemService.addMenuItem(menuItem);
    }

    @PutMapping("/updateMenuItem/{restaurantId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Integer restaurantId, @RequestBody MenuItem menuItem) {
        System.out.println("Received request with menuItem: " + menuItem);

        // Check if the restaurant is not null before accessing it
        if (menuItem.getRestaurant() != null) {
            // Handle the case where the restaurant is provided in the request
            return ResponseEntity.badRequest().body("Restaurant information should not be included in the request body.");
        }

        if (menuItem.getMenu_item_id() == null) {
            // Handle the case where the menu item ID is not provided in the request
            return ResponseEntity.badRequest().body("Menu item ID is required.");
        }

        if (menuItem.getName() == null || menuItem.getName().isEmpty()) {
            // Handle the case where the menu item name is not provided in the request
            return ResponseEntity.badRequest().body("Menu item name is required.");
        }

        if (menuItem.getDescription() == null || menuItem.getDescription().isEmpty()) {
            // Handle the case where the menu item description is not provided in the request
            return ResponseEntity.badRequest().body("Menu item description is required.");
        }

        if (menuItem.getPrice() == null || menuItem.getPrice() <= 0) {
            // Handle the case where the menu item price is not provided in the request
            return ResponseEntity.badRequest().body("Menu item price is required.");
        }

        // Create a new Restaurant object and set its ID
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);

        // Set the restaurant in the menuItem
        menuItem.setRestaurant(restaurant);

        return menuItemService.updateMenuItem(menuItem);
    }


}
