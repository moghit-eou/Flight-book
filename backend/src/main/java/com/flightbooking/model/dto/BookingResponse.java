package com.flightbooking.model.dto;

public class BookingResponse {
    private final Long id;
    private final String flightIata;
    private final String depIata;
    private final String arrIata;
    private final String status;

    public BookingResponse(Long id, String flightIata, String depIata, String arrIata, String status) {
        this.id = id;
        this.flightIata = flightIata;
        this.depIata = depIata;
        this.arrIata = arrIata;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getFlightIata() { return flightIata; }
    public String getDepIata() { return depIata; }
    public String getArrIata() { return arrIata; }
    public String getStatus() { return status; }
}