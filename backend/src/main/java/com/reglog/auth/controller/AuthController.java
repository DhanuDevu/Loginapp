package com.reglog.auth.controller;

import com.reglog.auth.dto.AuthResponse;
import com.reglog.auth.dto.LoginRequest;
import com.reglog.auth.dto.TokenValidationResponse;
import com.reglog.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User Login endpoint: verifies BCrypt password, generates JWT, saves in JWT_tokens table
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An unexpected error occurred during login."));
        }
    }

    /**
     * Validate JWT Token endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Token parameter is required."));
        }

        TokenValidationResponse validation = authService.validateToken(token);
        if (validation.isValid()) {
            return ResponseEntity.ok(validation);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validation);
        }
    }

    /**
     * Logout endpoint: removes token from JWT_tokens session store
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody(required = false) Map<String, String> requestBody) {
        String token = requestBody != null ? requestBody.get("token") : null;
        if (token != null) {
            authService.logout(token);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Successfully logged out."));
    }
}
