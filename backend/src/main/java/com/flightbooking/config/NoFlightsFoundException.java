package com.flightbooking.config;

public class NoFlightsFoundException extends RuntimeException {
    public NoFlightsFoundException(String depIata, String arrIata) {
        super("No flights found from " + depIata + (arrIata != null ? " to " + arrIata : ""));
    }
}