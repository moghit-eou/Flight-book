package com.flightbooking.model.dto;

public class BookingRequest {
    private String flightNumber;
    private String departureIata;
    private String arrivalIata;
    private String departureTime;
    private String arrivalTime;
    private String airlineName;
    private String passengerName;
    private String passengerEmail;
    private String passengerGender;
    private String passengerDob;
    private String passengerNationality;
    private String passengerIdType;
    private String passengerIdNumber;
    private String seatNumber;
    private String classType;
    private double price;
    private String baggageOption;
    private String bookingDate;

    public String getFlightNumber() { return flightNumber; }
    public String getDepartureIata() { return departureIata; }
    public String getArrivalIata() { return arrivalIata; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getAirlineName() { return airlineName; }
    public String getPassengerName() { return passengerName; }
    public String getPassengerEmail() { return passengerEmail; }
    public String getPassengerGender() { return passengerGender; }
    public String getPassengerDob() { return passengerDob; }
    public String getPassengerNationality() { return passengerNationality; }
    public String getPassengerIdType() { return passengerIdType; }
    public String getPassengerIdNumber() { return passengerIdNumber; }
    public String getSeatNumber() { return seatNumber; }
    public String getClassType() { return classType; }
    public double getPrice() { return price; }
    public String getBaggageOption() { return baggageOption; }
    public String getBookingDate() { return bookingDate; }
}