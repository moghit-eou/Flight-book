package com.flightbooking.model.dto;

public class ReviewRequest {
    private Long flightId;
    private int rating;
    private String comment;

    public Long getFlightId() { return flightId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}