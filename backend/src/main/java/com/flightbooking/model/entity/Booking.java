package com.flightbooking.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure_iata", nullable = false)
    private String departureIata;

    @Column(name = "arrival_iata", nullable = false)
    private String arrivalIata;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;

    @Column(name = "passenger_gender")
    private String passengerGender;

    @Column(name = "passenger_dob")
    private String passengerDob;

    @Column(name = "passenger_nationality")
    private String passengerNationality;

    @Column(name = "passenger_id_type")
    private String passengerIdType;

    @Column(name = "passenger_id_number")
    private String passengerIdNumber;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "class_type")
    private String classType;

    @Column(nullable = false)
    private double price;

    @Column(name = "baggage_option")
    private String baggageOption;

    @Column(nullable = false)
    private String status = "CONFIRMED";

    @Column(name = "booking_date")
    private String bookingDate;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Flight flight;

    

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getDepartureIata() { return departureIata; }
    public void setDepartureIata(String departureIata) { this.departureIata = departureIata; }

    public String getArrivalIata() { return arrivalIata; }
    public void setArrivalIata(String arrivalIata) { this.arrivalIata = arrivalIata; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }

    public String getPassengerGender() { return passengerGender; }
    public void setPassengerGender(String passengerGender) { this.passengerGender = passengerGender; }

    public String getPassengerDob() { return passengerDob; }
    public void setPassengerDob(String passengerDob) { this.passengerDob = passengerDob; }

    public String getPassengerNationality() { return passengerNationality; }
    public void setPassengerNationality(String passengerNationality) { this.passengerNationality = passengerNationality; }

    public String getPassengerIdType() { return passengerIdType; }
    public void setPassengerIdType(String passengerIdType) { this.passengerIdType = passengerIdType; }

    public String getPassengerIdNumber() { return passengerIdNumber; }
    public void setPassengerIdNumber(String passengerIdNumber) { this.passengerIdNumber = passengerIdNumber; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getBaggageOption() { return baggageOption; }
    public void setBaggageOption(String baggageOption) { this.baggageOption = baggageOption; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }



    public static class Builder {
        private User user;
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
        private Flight flight;

        public Builder user(User user) { this.user = user; return this; }
        public Builder flightNumber(String v) { this.flightNumber = v; return this; }
        public Builder departureIata(String v) { this.departureIata = v; return this; }
        public Builder arrivalIata(String v) { this.arrivalIata = v; return this; }
        public Builder departureTime(String v) { this.departureTime = v; return this; }
        public Builder arrivalTime(String v) { this.arrivalTime = v; return this; }
        public Builder airlineName(String v) { this.airlineName = v; return this; }
        public Builder passengerName(String v) { this.passengerName = v; return this; }
        public Builder passengerEmail(String v) { this.passengerEmail = v; return this; }
        public Builder passengerGender(String v) { this.passengerGender = v; return this; }
        public Builder passengerDob(String v) { this.passengerDob = v; return this; }
        public Builder passengerNationality(String v) { this.passengerNationality = v; return this; }
        public Builder passengerIdType(String v) { this.passengerIdType = v; return this; }
        public Builder passengerIdNumber(String v) { this.passengerIdNumber = v; return this; }
        public Builder seatNumber(String v) { this.seatNumber = v; return this; }
        public Builder classType(String v) { this.classType = v; return this; }
        public Builder price(double v) { this.price = v; return this; }
        public Builder baggageOption(String v) { this.baggageOption = v; return this; }
        public Builder bookingDate(String v) { this.bookingDate = v; return this; }
        public Builder flight(Flight v) { this.flight = v; return this; }

        public Booking build() {
            Booking b = new Booking();
            b.user = this.user;
            b.flightNumber = this.flightNumber;
            b.departureIata = this.departureIata;
            b.arrivalIata = this.arrivalIata;
            b.departureTime = this.departureTime;
            b.arrivalTime = this.arrivalTime;
            b.airlineName = this.airlineName;
            b.passengerName = this.passengerName;
            b.passengerEmail = this.passengerEmail;
            b.passengerGender = this.passengerGender;
            b.passengerDob = this.passengerDob;
            b.passengerNationality = this.passengerNationality;
            b.passengerIdType = this.passengerIdType;
            b.passengerIdNumber = this.passengerIdNumber;
            b.seatNumber = this.seatNumber;
            b.classType = this.classType;
            b.price = this.price;
            b.baggageOption = this.baggageOption;
            b.bookingDate = this.bookingDate;
            b.flight = this.flight;
            return b;
        }
    }
}
