package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;
import com.proyecto.challengejava.entity.Usuario;
import com.proyecto.challengejava.enums.Rol;
import com.proyecto.challengejava.repository.UsuarioRepository;
import com.proyecto.challengejava.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de autenticación y registro de usuarios.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que inyecta las dependencias necesarias para autenticación.
     *
     * @param usuarioRepository       Repositorio de usuarios.
     * @param jwtService              Servicio para generación de tokens JWT.
     * @param authenticationManager   Administrador de autenticación de Spring Security.
     * @param passwordEncoder         Codificador de contraseñas.
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
     * Registra un nuevo usuario con rol USER y retorna un token JWT.
     *
     * @param request Datos de registro del usuario (nombre, email, contraseña).
     * @return {@link AuthResponse} que contiene el token generado.
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.USER);

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail());

        return new AuthResponse(token);
    }

    /**
     * Autentica un usuario existente y retorna un token JWT si las credenciales son válidas.
     *
     * @param request Datos de autenticación (email y contraseña).
     * @return {@link AuthResponse} que contiene el token generado.
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