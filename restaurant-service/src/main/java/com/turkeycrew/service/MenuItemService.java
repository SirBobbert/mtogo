package com.turkeycrew.service;

import com.turkeycrew.model.MenuItem;
import com.turkeycrew.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

}