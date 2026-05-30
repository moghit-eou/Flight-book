package com.flightbooking.model.dto;

public class PaymentRequest {
    private Long bookingId;
    private double amount;
    private String method;

    public Long getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
}