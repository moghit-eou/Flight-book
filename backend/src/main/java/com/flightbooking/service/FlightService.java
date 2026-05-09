package com.flightbooking.model.service;

import com.flightbooking.model.dao.FlightDAO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class FlightService {

    private final FlightDAO flightDAO;

    public FlightService(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }


    public List<Object> getFlights(String depIata, String arrIata) {
   
        List<Object> flights = flightDAO.getFlights(depIata, arrIata);
        if (status == null || flights == null) return flights;
        
        return flights.stream()
            .filter(f -> status.equalsIgnoreCase(
                    (String) ((Map<?, ?>) f).get("flight_status")))
            .toList();
    }
    
}