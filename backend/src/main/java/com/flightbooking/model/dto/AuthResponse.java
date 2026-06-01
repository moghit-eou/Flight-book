package com.flightbooking.model.dto;

public class AuthResponse {
    private final String token;
    private final String role;
    private final String firstName;

    public AuthResponse(String token, String role, String firstName) {
        this.token = token;
        this.role = role;
        this.firstName = firstName;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
}