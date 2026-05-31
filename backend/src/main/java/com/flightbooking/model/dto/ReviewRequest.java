package com.flightbooking.model.dto;

public class ReviewRequest {
    private String flightIata;
    private int rating;
    private String comment;

    public String getFlightIata() { return flightIata; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}