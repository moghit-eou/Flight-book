package com.flightbooking.model.dto;

import com.flightbooking.model.entity.Review;
import java.time.LocalDateTime;

public class ReviewResponse {
    private final Long id;
    private final String userEmail;
    private final String flightIata;
    private final int rating;
    private final String comment;
    private final LocalDateTime createdAt;

    public ReviewResponse(Review r) {
        this.id = r.getId();
        this.userEmail = r.getUser().getEmail();
        this.flightIata = r.getFlight().getFlightIata();
        this.rating = r.getRating();
        this.comment = r.getComment();
        this.createdAt = r.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public String getFlightIata() { return flightIata; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}