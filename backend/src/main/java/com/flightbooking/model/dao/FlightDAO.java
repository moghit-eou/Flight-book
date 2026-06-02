package com.flightbooking.model.dao;
import java.util.List;

public interface FlightDAO {
    List<Object> getFlights(String depIata, String arrIata);
    void clearCache();  
}