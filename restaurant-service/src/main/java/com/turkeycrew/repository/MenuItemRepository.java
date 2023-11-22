package com.turkeycrew.repository;

import com.turkeycrew.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
