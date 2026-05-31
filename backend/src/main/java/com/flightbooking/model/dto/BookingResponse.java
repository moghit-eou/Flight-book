package com.flightbooking.model.dto;

import com.flightbooking.model.entity.Booking;

public class BookingResponse {
    private final Long id;
    private final String flightNumber;
    private final String departureIata;
    private final String arrivalIata;
    private final String departureTime;
    private final String arrivalTime;
    private final String airlineName;
    private final String passengerName;
    private final String passengerEmail;
    private final String classType;
    private final double price;
    private final String baggageOption;
    private final String status;
    private final String bookingDate;
    private final Long flightId;


    public BookingResponse(Booking b) {
        this.id = b.getId();
        this.flightNumber = b.getFlightNumber();
        this.departureIata = b.getDepartureIata();
        this.arrivalIata = b.getArrivalIata();
        this.departureTime = b.getDepartureTime();
        this.arrivalTime = b.getArrivalTime();
        this.airlineName = b.getAirlineName();
        this.passengerName = b.getPassengerName();
        this.passengerEmail = b.getPassengerEmail();
        this.classType = b.getClassType();
        this.price = b.getPrice();
        this.baggageOption = b.getBaggageOption();
        this.status = b.getStatus();
        this.bookingDate = b.getBookingDate();
        this.flightId = b.getFlight() != null ? b.getFlight().getId() : null;

    }

    public Long getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getDepartureIata() { return departureIata; }
    public String getArrivalIata() { return arrivalIata; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getAirlineName() { return airlineName; }
    public String getPassengerName() { return passengerName; }
    public String getPassengerEmail() { return passengerEmail; }
    public String getClassType() { return classType; }
    public double getPrice() { return price; }
    public String getBaggageOption() { return baggageOption; }
    public String getStatus() { return status; }
    public String getBookingDate() { return bookingDate; }
    public Long getFlightId() { return flightId; }

}