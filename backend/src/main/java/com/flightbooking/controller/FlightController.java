package com.flightbooking.controller;

import com.flightbooking.service.FlightService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public Object getFlights(
        @RequestParam(required = false) String dep_iata,
        @RequestParam(required = false) String arr_iata
    ) {
        return flightService.getFlights(dep_iata, arr_iata);
    }
}