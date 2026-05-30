package com.flightbooking.model.repository;

import com.flightbooking.model.entity.Payment;
import com.flightbooking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking(Booking booking);
}