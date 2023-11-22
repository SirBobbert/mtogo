package com.turkeycrew.service;

import com.turkeycrew.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuItem {

    private final MenuItemRepository menuItemRepository;

    public MenuItem(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }
}
