package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.AuthRequest;
import com.proyecto.challengejava.dto.AuthResponse;
import com.proyecto.challengejava.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
}
