package com.turkeycrew.controller;

import com.turkeycrew.model.MenuItem;
import com.turkeycrew.service.MenuItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurants/menu")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/find/{restaurantId}")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable Integer restaurantId) {
        return menuItemService.getRestaurantMenuById(restaurantId);
    }

    @PostMapping("/addMenuItem/{restaurantId}")
    public ResponseEntity<?> addMenuItem(@PathVariable Integer restaurantId, @RequestBody MenuItem menuItem) {
        return menuItemService.addMenuItem(restaurantId, menuItem);
    }

    @PutMapping("/updateMenuItem/{restaurantId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Integer restaurantId, @RequestBody MenuItem menuItem) {
        return menuItemService.updateMenuItem(restaurantId, menuItem);
    }

    @DeleteMapping("/deleteMenuItem/{menuItemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer menuItemId) {
        return menuItemService.deleteMenuItem(menuItemId);
    }


}
