package com.turkeycrew.repository;

import com.turkeycrew.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// In MenuItemRepository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByRestaurantRestaurantId(Integer restaurantId);
}


