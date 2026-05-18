package com.flightbooking.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String flightIata;

    @Column(nullable = false)
    private String depIata;

    @Column(nullable = false)
    private String arrIata;

    @Column(nullable = false)
    private String status = "CONFIRMED";

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFlightIata() { return flightIata; }
    public void setFlightIata(String flightIata) { this.flightIata = flightIata; }
    public String getDepIata() { return depIata; }
    public void setDepIata(String depIata) { this.depIata = depIata; }
    public String getArrIata() { return arrIata; }
    public void setArrIata(String arrIata) { this.arrIata = arrIata; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}