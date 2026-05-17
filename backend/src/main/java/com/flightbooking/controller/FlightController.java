package com.flightbooking.controller;

import com.flightbooking.config.NoFlightsFoundException;
import com.flightbooking.model.dto.FlightRequest;
import com.flightbooking.model.dto.FlightResponse;
import com.flightbooking.service.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public FlightResponse getFlights(
        @RequestParam(required = false) String dep_iata,
        @RequestParam(required = false) String arr_iata
    ) {
        FlightRequest request = new FlightRequest(
            dep_iata != null ? dep_iata.toUpperCase() : null,
            arr_iata != null ? arr_iata.toUpperCase() : null
        );
        request.validate();

        List<Object> flights = flightService.getFlights(request.getDepIata(), request.getArrIata());

        if (flights.isEmpty()) {
            throw new NoFlightsFoundException(request.getDepIata(), request.getArrIata());
        }

        return new FlightResponse(flights);
    }
}