package com.flightbooking.service;

import com.flightbooking.model.dto.AuthRequest;
import com.flightbooking.model.dto.AuthResponse;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.flightbooking.config.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void initDefaultAdmin() {
        if (userRepository.findByEmail("admin@skybook.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@skybook.com");
            admin.setPassword(encoder.encode("Admin123!"));
            admin.setRole("ADMIN");
            admin.setToken(UUID.randomUUID().toString());
            userRepository.save(admin);
            System.out.println("Default admin user created: admin@skybook.com");
        }
    }

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already in use");

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setPhoneNumber(request.getPhoneNumber());
        
        user.setEmail(request.getEmail());
        // CORRECTION ICI : Utilise encoder.encode()
        user.setPassword(encoder.encode(request.getPassword()));
        
        user.setRole("USER"); // Always USER from register endpoint
        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new AuthResponse(user.getToken(), user.getRole(), user.getFirstName());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new UnauthorizedException("Invalid email or password");

        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new AuthResponse(user.getToken(), user.getRole(), user.getFirstName());
    }

    public User validateToken(String token) {
        return userRepository.findByToken(token)
            .orElseThrow(() -> new UnauthorizedException("Invalid or expired token"));
    }
}