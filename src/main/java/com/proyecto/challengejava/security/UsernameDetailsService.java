package com.proyecto.challengejava.security;

import com.proyecto.challengejava.entity.Usuario;
import com.proyecto.challengejava.repository.UsernameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import static com.proyecto.challengejava.constants.Constants.*;

@Service
public class UsernameDetailsService implements UserDetailsService {

    private final UsernameRepository usernameRepository;

    @Autowired
    public UsernameDetailsService(UsernameRepository usernameRepository) {
        this.usernameRepository = usernameRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario username = usernameRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USUARIO_NO_ENCONTRADO + email));

        return User.withUsername(username.getEmail())
                .password(username.getPassword())
                .roles(username.getRole().name()) // Use the Role enum (ex: USER, ADMIN)
                .build();
    }
}
