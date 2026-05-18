package com.flightbooking.controller;

import com.flightbooking.model.dto.BookingRequest;
import com.flightbooking.model.dto.BookingResponse;
import com.flightbooking.service.BookingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse book(
        @RequestHeader("Authorization") String token,
        @RequestBody BookingRequest request
    ) {
        return bookingService.book(token, request);
    }

    @GetMapping
    public List<BookingResponse> getMyBookings(
        @RequestHeader("Authorization") String token
    ) {
        return bookingService.getMyBookings(token);
    }
}