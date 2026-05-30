package com.flightbooking.model.dto;

import com.flightbooking.model.entity.Payment;
import java.time.LocalDateTime;

public class PaymentResponse {
    private final Long id;
    private final Long bookingId;
    private final double amount;
    private final String method;
    private final String status;
    private final LocalDateTime paidAt;

    public PaymentResponse(Payment p) {
        this.id = p.getId();
        this.bookingId = p.getBooking().getId();
        this.amount = p.getAmount();
        this.method = p.getMethod();
        this.status = p.getStatus();
        this.paidAt = p.getPaidAt();
    }

    public Long getId() { return id; }
    public Long getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public LocalDateTime getPaidAt() { return paidAt; }
}