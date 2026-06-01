package com.flightbooking.service;

import com.flightbooking.model.dto.BookingRequest;
import com.flightbooking.model.dto.BookingResponse;
import com.flightbooking.model.entity.Booking;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.BookingRepository;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.flightbooking.model.repository.FlightRepository;
import java.util.Optional;
import com.flightbooking.model.entity.Flight;


import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;


    public BookingService(BookingRepository bookingRepository, 
        UserRepository userRepository,
        FlightRepository flightRepository) {

        this.bookingRepository = bookingRepository;
        this.flightRepository  = flightRepository;
        this.userRepository    = userRepository;
    }

    private User validateToken(String token) {
        return userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }

    public BookingResponse book(String token, BookingRequest request) {
        User user = validateToken(token);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlightNumber(request.getFlightNumber());
        booking.setDepartureIata(request.getDepartureIata());
        booking.setArrivalIata(request.getArrivalIata());
        booking.setDepartureTime(request.getDepartureTime());
        booking.setArrivalTime(request.getArrivalTime());
        booking.setAirlineName(request.getAirlineName());
        booking.setPassengerName(request.getPassengerName());
        booking.setPassengerEmail(request.getPassengerEmail());
        booking.setPassengerGender(request.getPassengerGender());
        booking.setPassengerDob(request.getPassengerDob());
        booking.setPassengerNationality(request.getPassengerNationality());
        booking.setPassengerIdType(request.getPassengerIdType());
        booking.setPassengerIdNumber(request.getPassengerIdNumber());
        booking.setSeatNumber(request.getSeatNumber());
        booking.setClassType(request.getClassType());
        booking.setPrice(request.getPrice());
        booking.setBaggageOption(request.getBaggageOption());
        booking.setBookingDate(request.getBookingDate());
        System.out.println("\n\n=== Looking for flight: " + request.getFlightNumber() + " ===");
        Optional<Flight> found = flightRepository.findByFlightIata(request.getFlightNumber());
        System.out.println("=== Found: " + found.isPresent() + " ===");
        found.ifPresent(f -> {
            System.out.println("=== Setting flight id: " + f.getId() + " ===");
            booking.setFlight(f);
        });
        bookingRepository.save(booking);
        System.out.println("=== Saved booking flight: " + booking.getFlight() + " ===\n\n");
        return new BookingResponse(booking);
    }

    public List<BookingResponse> getMyBookings(String token) {
        User user = validateToken(token);
        return bookingRepository.findByUser(user).stream()
            .map(BookingResponse::new)
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