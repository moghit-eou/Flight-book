package com.flightbooking.model.dto;

public class BookingRequest {
    private String flightIata;
    private String depIata;
    private String arrIata;

    public String getFlightIata() { return flightIata; }
    public String getDepIata() { return depIata; }
    public String getArrIata() { return arrIata; }
}