package com.turkeycrew.repository;

import com.turkeycrew.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByUserId(Integer userId);

    List<Feedback> findByRestaurantId(Integer restaurantId);
}
