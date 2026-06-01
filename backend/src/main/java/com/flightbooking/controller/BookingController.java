package com.flightbooking.controller;

import com.flightbooking.model.entity.Booking;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.BookingRepository;
import com.flightbooking.model.repository.UserRepository;
import com.flightbooking.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public BookingController(BookingRepository bookingRepository, UserRepository userRepository, AuthService authService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    private User resolveUser(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new IllegalArgumentException("Token d'authentification manquant");
        }
        String token = authHeader;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return authService.validateToken(token);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Booking booking
    ) {
        User user = resolveUser(authHeader);
        booking.setUser(user);
        
        // Formater la date de réservation actuelle
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        booking.setBookingDate(LocalDateTime.now().format(formatter));
        booking.setStatus("CONFIRMED");
        
        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(
            @RequestHeader("Authorization") String authHeader
    ) {
        User user = resolveUser(authHeader);
        List<Booking> myBookings = bookingRepository.findByUser(user);
        return ResponseEntity.ok(myBookings);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Booking>> getAllBookings(
            @RequestHeader("Authorization") String authHeader
    ) {
        User user = resolveUser(authHeader);
        if (!"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Accès refusé. Rôle administrateur requis.");
        }
        List<Booking> allBookings = bookingRepository.findAll();
        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/admin/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats(
            @RequestHeader("Authorization") String authHeader
    ) {
        User user = resolveUser(authHeader);
        if (!"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Accès refusé. Rôle administrateur requis.");
        }

        List<Booking> bookings = bookingRepository.findAll();
        long totalBookings = bookings.size();
        double totalRevenue = bookings.stream()
                .filter(b -> !"CANCELLED".equals(b.getStatus()))
                .mapToDouble(Booking::getPrice)
                .sum();
        
        long totalUsers = userRepository.count();
        double averagePrice = totalBookings > 0 ? totalRevenue / totalBookings : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", totalBookings);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalUsers", totalUsers);
        stats.put("averagePrice", Math.round(averagePrice * 100.0) / 100.0);

        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelBooking(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) {
        User user = resolveUser(authHeader);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        // Vérifier les droits : l'utilisateur doit être le propriétaire de la réservation ou un ADMIN
        if (!booking.getUser().getId().equals(user.getId()) && !"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Accès non autorisé pour annuler cette réservation.");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        Map<String, String> response = new HashMap<>();
        response.put("message", "La réservation a été annulée avec succès");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/flew")
    public ResponseEntity<Map<String, String>> markAsFlewBooking(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id
    ) {
        User user = resolveUser(authHeader);
        if (!"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Accès refusé. Rôle administrateur requis.");
        }
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        booking.setStatus("FLEW");
        bookingRepository.save(booking);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Le vol a été marqué comme effectué");
        return ResponseEntity.ok(response);
    }
}
