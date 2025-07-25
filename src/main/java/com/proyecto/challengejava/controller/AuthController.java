package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;
import com.proyecto.challengejava.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para manejo de autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor que inyecta el servicio de autenticación.
     *
     * @param authService Servicio encargado de la lógica de autenticación y registro.
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request Objeto con los datos de registro (nombre, email, contraseña).
     * @return Respuesta con los datos del usuario autenticado y el token JWT.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint para autenticar un usuario existente.
     *
     * @param request Objeto con las credenciales del usuario (email y contraseña).
     * @return Respuesta con los datos del usuario autenticado y el token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}