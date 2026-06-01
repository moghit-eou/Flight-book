package com.flightbooking.model.dto;

public class AuthRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String phoneNumber;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPhoneNumber() { return phoneNumber; }
}