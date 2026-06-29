package com.flightbooking.model.dao;

import com.flightbooking.model.entity.Flight;
import java.util.Map;

public class AviationStackFlightAdapter {

    private final Map<String, Object> rawFlight;

    public AviationStackFlightAdapter(Map<String, Object> rawFlight) {
        this.rawFlight = rawFlight;
    }

    @SuppressWarnings("unchecked")
    public Flight toFlight() {
        String flightDate   = (String) rawFlight.get("flight_date");
        String flightStatus = (String) rawFlight.get("flight_status");

        Map<String, Object> departure = (Map<String, Object>) rawFlight.get("departure");
        String depIata      = departure != null ? (String) departure.get("iata") : null;
        String depAirport   = departure != null ? (String) departure.get("airport") : null;
        String depScheduled = departure != null ? (String) departure.get("scheduled") : null;

        Map<String, Object> arrival = (Map<String, Object>) rawFlight.get("arrival");
        String arrIata      = arrival != null ? (String) arrival.get("iata") : null;
        String arrAirport   = arrival != null ? (String) arrival.get("airport") : null;
        String arrScheduled = arrival != null ? (String) arrival.get("scheduled") : null;

        Map<String, Object> airline = (Map<String, Object>) rawFlight.get("airline");
        String airlineName  = airline != null ? (String) airline.get("name") : null;

        Map<String, Object> flightInfo = (Map<String, Object>) rawFlight.get("flight");
        String flightIata   = flightInfo != null ? (String) flightInfo.get("iata") : null;

        if (depIata == null || arrIata == null) return null;

        return Flight.builder()
                .flightDate(flightDate)
                .flightStatus(flightStatus)
                .depIata(depIata.toUpperCase())
                .arrIata(arrIata.toUpperCase())
                .depAirport(depAirport)
                .arrAirport(arrAirport)
                .depScheduled(depScheduled)
                .arrScheduled(arrScheduled)
                .airlineName(airlineName)
                .flightIata(flightIata)
                .build()
    }

    @SuppressWarnings("unchecked")
    public String getFlightIata() {
        Map<String, Object> flightInfo = (Map<String, Object>) rawFlight.get("flight");
        return flightInfo != null ? (String) flightInfo.get("iata") : null;
    }

    public String getFlightDate() {
        return (String) rawFlight.get("flight_date");
    }
}
