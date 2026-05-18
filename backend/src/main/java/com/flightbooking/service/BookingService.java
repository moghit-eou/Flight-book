package com.flightbooking.service;

import com.flightbooking.model.dto.BookingRequest;
import com.flightbooking.model.dto.BookingResponse;
import com.flightbooking.model.entity.Booking;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.BookingRepository;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    private User validateToken(String token) {
        return userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }

    public BookingResponse book(String token, BookingRequest request) {
        User user = validateToken(token);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlightIata(request.getFlightIata());
        booking.setDepIata(request.getDepIata());
        booking.setArrIata(request.getArrIata());
        bookingRepository.save(booking);
        return new BookingResponse(booking.getId(), booking.getFlightIata(),
            booking.getDepIata(), booking.getArrIata(), booking.getStatus());
    }

    public List<BookingResponse> getMyBookings(String token) {
        User user = validateToken(token);
        return bookingRepository.findByUser(user).stream()
            .map(b -> new BookingResponse(b.getId(), b.getFlightIata(),
                b.getDepIata(), b.getArrIata(), b.getStatus()))
            .toList();
    }


    public void cancel(String token, Long id) {
        User user = validateToken(token);
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("Not your booking");

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }
}