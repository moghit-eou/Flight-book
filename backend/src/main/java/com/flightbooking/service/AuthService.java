package com.flightbooking.service;

import com.flightbooking.model.dto.AuthRequest;
import com.flightbooking.model.dto.AuthResponse;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse register(AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already in use");

        User user = new User();
        user.setEmail(request.getEmail());
        //user.setPassword(encoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());
        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user);
        return new AuthResponse(user.getToken(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!request.getPassword().equals(user.getPassword()))
            throw new IllegalArgumentException("Invalid email or password");

        user.setToken(UUID.randomUUID().toString());
        userRepository.save(user); // update
        return new AuthResponse(user.getToken(), user.getRole());
    }

    public User validateToken(String token) {
        return userRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));
    }
}