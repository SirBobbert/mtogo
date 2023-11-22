package com.turkeycrew.service;

import com.turkeycrew.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
}
