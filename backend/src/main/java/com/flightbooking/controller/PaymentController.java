package com.flightbooking.controller;

import com.flightbooking.model.dto.PaymentRequest;
import com.flightbooking.model.dto.PaymentResponse;
import com.flightbooking.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse pay(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody PaymentRequest request
    ) {
        return paymentService.pay(authHeader, request); // return a PaymentResponse 
    }

    @GetMapping("/{bookingId}")
    public PaymentResponse getByBookingId(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long bookingId
    ) {
        return paymentService.getByBookingId(authHeader, bookingId);
    }
}