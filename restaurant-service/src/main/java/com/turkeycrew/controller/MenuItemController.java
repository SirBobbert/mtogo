package com.turkeycrew.controller;

import com.turkeycrew.model.MenuItem;
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
