package com.turkeycrew.controller;

import com.turkeycrew.model.Feedback;
import com.turkeycrew.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurants/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/addFeedback")
    public ResponseEntity<?> addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.addFeedback(feedback);
    }

    @GetMapping("/getFeedback/{feedbackId}")
    public ResponseEntity<?> getFeedback(@PathVariable Integer feedbackId) {
        return feedbackService.getFeedback(feedbackId);
    }
}

