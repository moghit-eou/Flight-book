package com.flightbooking.controller;

import com.flightbooking.model.dto.ReviewRequest;
import com.flightbooking.model.dto.ReviewResponse;
import com.flightbooking.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse addReview(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody ReviewRequest request
    ) {
        return reviewService.addReview(authHeader, request);
    }

    @GetMapping("/flight/{flightId}")
    public List<ReviewResponse> getByFlight(@PathVariable Long flightId) {
        return reviewService.getByFlight(flightId);
    }

    @GetMapping("/my-reviews")
    public List<ReviewResponse> getMyReviews(
        @RequestHeader("Authorization") String authHeader
    ) {
        return reviewService.getMyReviews(authHeader);
    }

    @GetMapping("/admin/all")
    public List<ReviewResponse> getAllReviews(
        @RequestHeader("Authorization") String authHeader
    ) {
        return reviewService.getAllReviews(authHeader);
    }
    
}