package com.flightbooking.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flights", indexes = {
    @Index(name = "idx_flight_dep_arr", columnList = "depIata, arrIata")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
}
