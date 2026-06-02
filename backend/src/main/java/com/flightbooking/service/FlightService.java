package com.flightbooking.service;

import com.flightbooking.model.dao.FlightDAO;
import org.springframework.stereotype.Service;
import com.flightbooking.model.repository.UserRepository;
import com.flightbooking.config.UnauthorizedException;

import java.util.List;

@Service
public class FlightService {

    private final FlightDAO flightDAO;
    private final UserRepository userRepository;


    public FlightService(FlightDAO flightDAO, UserRepository userRepository) {
        this.flightDAO = flightDAO;
        this.userRepository = userRepository;
    }

    public List<Object> getFlights(String depIata, String arrIata) {
        return flightDAO.getFlights(depIata, arrIata);
    }


    public void clearCache(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
     
        var user = userRepository.findByToken(token)
            .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        if (!"ADMIN".equals(user.getRole()))
            throw new UnauthorizedException("Admin role required");
        flightDAO.clearCache();
    }
}