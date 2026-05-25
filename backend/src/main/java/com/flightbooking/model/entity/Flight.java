package com.flightbooking.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "flights", indexes = {
    @Index(name = "idx_flight_dep_arr", columnList = "depIata, arrIata")
})
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_date")
    private String flightDate;

    @Column(name = "flight_status")
    private String flightStatus;

    @Column(name = "dep_iata", nullable = false)
    private String depIata;

    @Column(name = "arr_iata", nullable = false)
    private String arrIata;

    @Column(name = "dep_airport")
    private String depAirport;

    @Column(name = "arr_airport")
    private String arrAirport;

    @Column(name = "dep_scheduled")
    private String depScheduled;

    @Column(name = "arr_scheduled")
    private String arrScheduled;

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "flight_iata")
    private String flightIata;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

    public String getFlightStatus() { return flightStatus; }
    public void setFlightStatus(String flightStatus) { this.flightStatus = flightStatus; }

    public String getDepIata() { return depIata; }
    public void setDepIata(String depIata) { this.depIata = depIata; }

    public String getArrIata() { return arrIata; }
    public void setArrIata(String arrIata) { this.arrIata = arrIata; }

    public String getDepAirport() { return depAirport; }
    public void setDepAirport(String depAirport) { this.depAirport = depAirport; }

    public String getArrAirport() { return arrAirport; }
    public void setArrAirport(String arrAirport) { this.arrAirport = arrAirport; }

    public String getDepScheduled() { return depScheduled; }
    public void setDepScheduled(String depScheduled) { this.depScheduled = depScheduled; }

    public String getArrScheduled() { return arrScheduled; }
    public void setArrScheduled(String arrScheduled) { this.arrScheduled = arrScheduled; }

    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }

    public String getFlightIata() { return flightIata; }
    public void setFlightIata(String flightIata) { this.flightIata = flightIata; }
}
