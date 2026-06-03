package com.flightbooking.service;

import com.flightbooking.config.UnauthorizedException;
import com.flightbooking.model.dto.AuthRequest;
import com.flightbooking.model.dto.AuthResponse;
import com.flightbooking.model.entity.User;
import com.flightbooking.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 *
 * File location (relative to backend/):
 *   src/test/java/com/flightbooking/service/AuthServiceTest.java
 *
 * No Spring context is loaded — pure unit tests with Mockito.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ── shared helpers ──────────────────────────────────────────────────────

    /** Builds a minimal AuthRequest (register shape) */
    private AuthRequest buildRegisterRequest(String email, String password) {
        AuthRequest req = new AuthRequest();
        req.setEmail(email);
        req.setPassword(password);
        req.setFirstName("Youssef");
        req.setLastName("Benali");
        req.setCity("Casablanca");
        req.setCountry("Morocco");
        req.setPhoneNumber("+212600000000");
        return req;
    }

    /** Builds a minimal AuthRequest (login shape) */
    private AuthRequest buildLoginRequest(String email, String password) {
        AuthRequest req = new AuthRequest();
        req.setEmail(email);
        req.setPassword(password);
        return req;
    }

    /** Creates a persisted-like User with a BCrypt-hashed password */
    private User buildStoredUser(String email, String rawPassword, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(rawPassword));
        user.setRole(role);
        user.setFirstName("Youssef");
        user.setToken("existing-token-123");
        return user;
    }

    // ════════════════════════════════════════════════════════════════════════
    // register()
    // ════════════════════════════════════════════════════════════════════════

    // ── NORMAL ──────────────────────────────────────────────────────────────

    @Test
    void register_withNewEmail_returnsTokenAndUserRole() {
        when(userRepository.findByEmail("youssef@skybook.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthRequest req = buildRegisterRequest("youssef@skybook.com", "Secret99!");
        AuthResponse resp = authService.register(req);

        // assertEquals — role is always USER from the register endpoint
        assertEquals("USER", resp.getRole());

        // assertEquals — firstName is mapped correctly
        assertEquals("Youssef", resp.getFirstName());

        // assertTrue — a UUID token was generated (not blank)
        assertTrue(resp.getToken() != null && !resp.getToken().isBlank());
    }

    @Test
    void register_withNewEmail_passwordIsStoredHashed() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Capture the User passed to save()
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User saved = inv.getArgument(0);

            // assertTrue — stored password must NOT equal the raw password
            assertTrue(encoder.matches("Secret99!", saved.getPassword()),
                    "Stored password must be a valid BCrypt hash of the raw password");

            return saved;
        });

        authService.register(buildRegisterRequest("youssef@skybook.com", "Secret99!"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ── ERROR ───────────────────────────────────────────────────────────────

    @Test
    void register_withDuplicateEmail_throwsIllegalArgumentException() {
        when(userRepository.findByEmail("duplicate@skybook.com"))
                .thenReturn(Optional.of(new User()));

        AuthRequest req = buildRegisterRequest("duplicate@skybook.com", "Password1!");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(req)
        );

        // assertEquals — exact message the frontend error handler relies on
        assertEquals("Email already in use", ex.getMessage());
    }

    // ════════════════════════════════════════════════════════════════════════
    // login()
    // ════════════════════════════════════════════════════════════════════════

    // ── NORMAL ──────────────────────────────────────────────────────────────

    @Test
    void login_withCorrectCredentials_returnsNewToken() {
        User stored = buildStoredUser("youssef@skybook.com", "Secret99!", "USER");
        when(userRepository.findByEmail("youssef@skybook.com")).thenReturn(Optional.of(stored));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthResponse resp = authService.login(buildLoginRequest("youssef@skybook.com", "Secret99!"));

        // assertTrue — a fresh token is generated on every login (not null/blank)
        assertTrue(resp.getToken() != null && !resp.getToken().isBlank());

        // assertEquals — role is preserved from the stored user
        assertEquals("USER", resp.getRole());
    }

    @Test
    void login_withAdminCredentials_returnsAdminRole() {
        User admin = buildStoredUser("admin@skybook.com", "Admin123!", "ADMIN");
        when(userRepository.findByEmail("admin@skybook.com")).thenReturn(Optional.of(admin));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthResponse resp = authService.login(buildLoginRequest("admin@skybook.com", "Admin123!"));

        // assertEquals — ADMIN role must pass through correctly
        assertEquals("ADMIN", resp.getRole());
    }

    // ── ERROR ───────────────────────────────────────────────────────────────

    @Test
    void login_withUnknownEmail_throwsUnauthorizedException() {
        when(userRepository.findByEmail("ghost@skybook.com")).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(buildLoginRequest("ghost@skybook.com", "anything"))
        );

        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void login_withWrongPassword_throwsUnauthorizedException() {
        User stored = buildStoredUser("youssef@skybook.com", "Secret99!", "USER");
        when(userRepository.findByEmail("youssef@skybook.com")).thenReturn(Optional.of(stored));

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authService.login(buildLoginRequest("youssef@skybook.com", "WrongPass!"))
        );

        assertEquals("Invalid email or password", ex.getMessage());
    }

    // ── EDGE ────────────────────────────────────────────────────────────────

    @Test
    void login_tokenIsRefreshedOnEveryLogin() {
        User stored = buildStoredUser("youssef@skybook.com", "Secret99!", "USER");
        when(userRepository.findByEmail("youssef@skybook.com")).thenReturn(Optional.of(stored));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthResponse first  = authService.login(buildLoginRequest("youssef@skybook.com", "Secret99!"));

        // Reset the stored token to simulate a second login call
        stored.setToken(first.getToken());
        AuthResponse second = authService.login(buildLoginRequest("youssef@skybook.com", "Secret99!"));

        // assertTrue — each login produces a unique token (session rotation)
        assertTrue(!first.getToken().equals(second.getToken()),
                "Token must be rotated on every login");
    }

    // ════════════════════════════════════════════════════════════════════════
    // validateToken()
    // ════════════════════════════════════════════════════════════════════════

    // ── NORMAL ──────────────────────────────────────────────────────────────

    @Test
    void validateToken_withValidToken_returnsCorrectUser() {
        User stored = buildStoredUser("youssef@skybook.com", "Secret99!", "USER");
        stored.setToken("valid-uuid-token");
        when(userRepository.findByToken("valid-uuid-token")).thenReturn(Optional.of(stored));

        User result = authService.validateToken("valid-uuid-token");

        // assertEquals — the returned user matches what the repository has
        assertEquals("youssef@skybook.com", result.getEmail());
    }

    // ── ERROR ───────────────────────────────────────────────────────────────

    @Test
    void validateToken_withInvalidToken_throwsUnauthorizedException() {
        when(userRepository.findByToken("bad-token")).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authService.validateToken("bad-token")
        );

        assertEquals("Invalid or expired token", ex.getMessage());
    }

    // ── EDGE ────────────────────────────────────────────────────────────────

    @Test
    void validateToken_withEmptyString_throwsUnauthorizedException() {
        when(userRepository.findByToken("")).thenReturn(Optional.empty());

        assertThrows(
                UnauthorizedException.class,
                () -> authService.validateToken("")
        );
    }
}