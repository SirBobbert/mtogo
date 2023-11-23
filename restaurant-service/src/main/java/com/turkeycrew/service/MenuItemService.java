package com.turkeycrew.service;

import com.turkeycrew.model.MenuItem;
import com.turkeycrew.repository.MenuItemRepository;
import com.turkeycrew.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public ResponseEntity<?> getRestaurantMenuById(Integer restaurantId) {

        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant ID is required.");
        }

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantRestaurantId(restaurantId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    public ResponseEntity<?> addMenuItem(Integer restaurantId, MenuItem menuItem) {

        if (!restaurantRepository.existsById(restaurantId) || !menuItemRepository.existsById(menuItem.getMenu_item_id())) {
            return ResponseEntity.badRequest().body("Both restaurant ID and menu item ID are required.");
        }

        if (menuItem.getName() == null || menuItem.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Menu item name is required.");
        }

        if (menuItem.getDescription() == null || menuItem.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("Menu item description is required.");
        }

        if (menuItem.getPrice() == null || menuItem.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("Menu item price is required.");
        }

        menuItem.setRestaurantId(restaurantId);
        menuItemRepository.save(menuItem);
        return ResponseEntity.ok("MenuItem added successfully");
    }

    public ResponseEntity<?> updateMenuItem(Integer restaurantId, MenuItem menuItem) {

        if (!restaurantRepository.existsById(restaurantId) || !menuItemRepository.existsById(menuItem.getMenu_item_id())) {
            return ResponseEntity.badRequest().body("Both restaurant ID and menu item ID are required.");
        }

        if (menuItem.getName() == null || menuItem.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Menu item name is required.");
        }

        if (menuItem.getDescription() == null || menuItem.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("Menu item description is required.");
        }

        if (menuItem.getPrice() == null || menuItem.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("Menu item price is required.");
        }

        menuItem.setRestaurantId(restaurantId);
        menuItemRepository.save(menuItem);
        return ResponseEntity.ok("MenuItem updated successfully");
    }


    public ResponseEntity<?> deleteMenuItem(Integer menuItemId) {

        if (!menuItemRepository.existsById(menuItemId)) {
            return ResponseEntity.badRequest().body("Menu item ID is required.");
        }

        menuItemRepository.deleteById(menuItemId);
        return ResponseEntity.ok("MenuItem deleted successfully");
    }
}