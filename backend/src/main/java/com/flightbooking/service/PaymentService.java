package com.flightbooking.service;

import com.flightbooking.model.dto.PaymentRequest;
import com.flightbooking.model.dto.PaymentResponse;
import com.flightbooking.model.entity.Booking;
import com.flightbooking.model.entity.Payment;
import com.flightbooking.model.repository.BookingRepository;
import com.flightbooking.model.repository.PaymentRepository;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    private String resolveToken(String authHeader) {
        return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
    }

    public PaymentResponse pay(String authHeader, PaymentRequest request) {
        String token = resolveToken(authHeader);
        userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        paymentRepository.save(payment);
        return new PaymentResponse(payment);
    }

    public PaymentResponse getByBookingId(String authHeader, Long bookingId) {
        String token = resolveToken(authHeader);
        userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        Payment payment = paymentRepository.findByBooking(booking)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        return new PaymentResponse(payment);
    }
}