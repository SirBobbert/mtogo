package com.turkeycrew.service;

import com.turkeycrew.model.Feedback;
import com.turkeycrew.repository.FeedbackRepository;
import com.turkeycrew.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RestaurantRepository restaurantRepository;


    public ResponseEntity<?> addFeedback(Feedback feedback) {

        // Needs to check if the user exists
        if (feedback.getUserId() < 1) {
            return ResponseEntity.badRequest().body("User ID is required.");
        }

        // Needs to check if the restaurant exists
        if (feedback.getRestaurantId() < 1) {
            return ResponseEntity.badRequest().body("Restaurant ID is required.");
        }

        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        if (feedback.getComments().length() > 250) {
            return ResponseEntity.badRequest().body("Comments must be less than 250 characters.");
        }


        return ResponseEntity.ok(feedbackRepository.save(feedback));
    }

    public ResponseEntity<?> getFeedback(Integer feedbackId) {

        if (!feedbackRepository.existsById(feedbackId)) {
            return ResponseEntity.badRequest().body("Feedback ID does not exist.");
        }

        return new ResponseEntity<>(feedbackRepository.findById(feedbackId), HttpStatus.OK);
    }

    // TODO: CustomerId is required from customer-service (kafka?)
    public ResponseEntity<?> getFeedbackByUser(Integer userId) {
        // Validate userId
        if (userId < 1) {
            return ResponseEntity.badRequest().body("User ID is required.");
        }

        // Assuming feedbackRepository has a method to retrieve feedback by userId
        List<Feedback> feedbackList = feedbackRepository.findByUserId(userId);

        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }

    // TODO: CustomerId is required from customer-service (kafka?)
    public ResponseEntity<?> getFeedbackByRestaurant(Integer restaurantId) {

        if (!restaurantRepository.existsById(restaurantId)) {
            return ResponseEntity.badRequest().body("Restaurant ID does not exist.");
        }

        List<Feedback> feedbackList = feedbackRepository.findByRestaurantId(restaurantId);

        return new ResponseEntity<>(feedbackList, HttpStatus.OK);

    }
}
