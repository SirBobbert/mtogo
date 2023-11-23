package com.turkeycrew.controller;

import com.turkeycrew.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/find/{restaurantId}")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable Integer restaurantId) {
        return menuItemService.getRestaurantMenuById(restaurantId);
    }


}
