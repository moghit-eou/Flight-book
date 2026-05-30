package com.flightbooking.service;

import com.flightbooking.model.dto.ReviewRequest;
import com.flightbooking.model.dto.ReviewResponse;
import com.flightbooking.model.entity.Flight;
import com.flightbooking.model.entity.Review;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.FlightRepository;
import com.flightbooking.model.repository.ReviewRepository;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         FlightRepository flightRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
    }

    private User resolveUser(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }

    public ReviewResponse addReview(String authHeader, ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5");

        User user = resolveUser(authHeader);
        Flight flight = flightRepository.findById(request.getFlightId())
            .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        Review review = new Review();
        review.setUser(user);
        review.setFlight(flight);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);
        return new ReviewResponse(review);
    }

    public List<ReviewResponse> getByFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new IllegalArgumentException("Flight not found"));
        return reviewRepository.findByFlight(flight).stream()
            .map(ReviewResponse::new)
            .toList();
    }

    public List<ReviewResponse> getMyReviews(String authHeader) {
        User user = resolveUser(authHeader);
        return reviewRepository.findByUser(user).stream()
            .map(ReviewResponse::new)
            .toList();
    }
}