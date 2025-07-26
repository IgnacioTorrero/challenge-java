package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsernameRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

