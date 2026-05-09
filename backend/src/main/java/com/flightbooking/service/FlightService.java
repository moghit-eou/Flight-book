package com.flightbooking.model.service;

import com.flightbooking.model.dao.FlightDAO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FlightService {

    private final FlightDAO flightDAO;

    public FlightService(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public List<Object> getFlights(String depIata, String arrIata) {
        return flightDAO.getFlights(depIata, arrIata);
    }
}