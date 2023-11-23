package com.turkeycrew.repository;

import com.turkeycrew.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    boolean existsByName(String name);

    boolean existsByAddress(String address);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    boolean existsByWebsite(String website);
}
