package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;
import com.proyecto.challengejava.entity.Username;
import com.proyecto.challengejava.enums.Role;
import com.proyecto.challengejava.repository.UsernameRepository;
import com.proyecto.challengejava.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the authentication and user registration service.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UsernameRepository usernameRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor that injects the dependencies required for authentication.
     *
     * @param usernameRepository       Repository for users.
     * @param jwtService              Service for generating JWT tokens.
     * @param authenticationManager   Spring Security authentication manager.
     * @param passwordEncoder         Password encoder.
     */
    @Autowired
    public AuthServiceImpl(UsernameRepository usernameRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder) {
        this.usernameRepository = usernameRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the USER role and returns a JWT token.
     *
     * @param request User registration data (name, email, password).
     * @return {@link AuthResponse} containing the generated token.
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        Username username = new Username();
        username.setName(request.getName());
        username.setEmail(request.getEmail());
        username.setPassword(passwordEncoder.encode(request.getPassword()));
        username.setRole(Role.USER);

        usernameRepository.save(username);

        String token = jwtService.generateToken(username.getEmail());

        return new AuthResponse(token);
    }

    /**
     * Authenticates an existing user and returns a JWT token if the credentials are valid.
     *
     * @param request Authentication data (email and password).
     * @return {@link AuthResponse} containing the generated token.
     */
    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails.getUsername());

        return new AuthResponse(token);
    }
}