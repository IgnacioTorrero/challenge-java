package com.proyecto.challengejava.security;

import com.proyecto.challengejava.entity.Usuario;
import com.proyecto.challengejava.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import static com.proyecto.challengejava.constants.Constants.*;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USUARIO_NO_ENCONTRADO + email));

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name()) // Usa el enum Rol (ej: USER, ADMIN)
                .build();
    }
}
