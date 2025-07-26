package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;
import com.proyecto.challengejava.entity.Usuario;
import com.proyecto.challengejava.enums.Role;
import com.proyecto.challengejava.repository.UsuarioRepository;
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

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor that injects the dependencies required for authentication.
     *
     * @param usuarioRepository       Repository for users.
     * @param jwtService              Service for generating JWT tokens.
     * @param authenticationManager   Spring Security authentication manager.
     * @param passwordEncoder         Password encoder.
     */
    @Autowired
    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
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
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getName());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Role.USER);

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail());

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