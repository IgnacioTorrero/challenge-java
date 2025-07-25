package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;
import com.proyecto.challengejava.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor that injects the authentication service.
     *
     * @param authService Service responsible for authentication and registration logic.
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to register a new user.
     *
     * @param request Object containing registration data (name, email, password).
     * @return Response with authenticated user data and the JWT token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint to authenticate an existing user.
     *
     * @param request Object containing user credentials (email and password).
     * @return Response with authenticated user data and the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}