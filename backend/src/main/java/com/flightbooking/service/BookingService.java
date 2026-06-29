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
        Optional<Flight> found = flightRepository.findByFlightIata(request.getFlightNumber());
        Flight flight = found.orElse(null);
        
        if (flight != null) {
            System.out.println("=== Setting flight id: " + flight.getId() + " ===");
        }
        
        Booking booking = new Booking.Builder()
        .user(user)
        .flightNumber(request.getFlightNumber())
        .departureIata(request.getDepartureIata())
        .arrivalIata(request.getArrivalIata())
        .departureTime(request.getDepartureTime())
        .arrivalTime(request.getArrivalTime())
        .airlineName(request.getAirlineName())
        .passengerName(request.getPassengerName())
        .passengerEmail(request.getPassengerEmail())
        .passengerGender(request.getPassengerGender())
        .passengerDob(request.getPassengerDob())
        .passengerNationality(request.getPassengerNationality())
        .passengerIdType(request.getPassengerIdType())
        .passengerIdNumber(request.getPassengerIdNumber())
        .seatNumber(request.getSeatNumber())
        .classType(request.getClassType())
        .price(request.getPrice())
        .baggageOption(request.getBaggageOption())
        .bookingDate(request.getBookingDate())
        .flight(found.orElse(null))
        .build();
        
        bookingRepository.save(booking);
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