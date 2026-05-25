package com.flightbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class IndexController {

    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
            "name", "SkyBook flight-booking-backend API",
            "status", "Running successfully",
            "port", 8080,
            "frontend_url", "http://localhost:4200",
            "message", "Welcome to SkyBook! The backend is running. Please access the application via your browser at http://localhost:4200"
        );
    }
}
