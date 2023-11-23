package com.turkeycrew.service;

import com.turkeycrew.model.MenuItem;
import com.turkeycrew.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public ResponseEntity<?> getRestaurantMenuById(Integer restaurantId) {
        List<MenuItem> menuItems = menuItemRepository.findByRestaurantRestaurantId(restaurantId);
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    public ResponseEntity<?> addMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
        return ResponseEntity.ok("MenuItem added successfully");
    }

    public ResponseEntity<?> updateMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
        return ResponseEntity.ok("MenuItem updated successfully");
    }


    public ResponseEntity<?> deleteMenuItem(Integer restaurantId, Integer menuItemId) {
        // Add logic to check if the menu item belongs to the specified restaurant
        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(menuItemId);

        if (menuItemOptional.isPresent()) {
            MenuItem menuItem = menuItemOptional.get();
            if (menuItem.getRestaurant() != null && menuItem.getRestaurant().getRestaurantId().equals(restaurantId)) {
                menuItemRepository.deleteById(menuItemId);
                return ResponseEntity.ok("MenuItem deleted successfully");
            } else {
                return ResponseEntity.badRequest().body("Menu item does not belong to the specified restaurant.");
            }
        } else {
            return ResponseEntity.badRequest().body("Menu item not found.");
        }
    }

}