package com.flightbooking.model.dto;

public class FlightRequest {

    private final String depIata;
    private final String arrIata;

    public FlightRequest(String depIata, String arrIata) {
        this.depIata = depIata;
        this.arrIata = arrIata;
    }

    public String getDepIata() { return depIata; }
    public String getArrIata() { return arrIata; }

    public void validate() {
        if (depIata != null && !depIata.isBlank() && !depIata.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Invalid dep_iata: must be 3 uppercase letters (e.g. CDG)");
        }
        if (arrIata != null && !arrIata.isBlank() && !arrIata.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Invalid arr_iata: must be 3 uppercase letters (e.g. JFK)");
        }
    }
}