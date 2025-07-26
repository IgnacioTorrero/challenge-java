package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsernameRepository extends JpaRepository<Username, Long> {
    Optional<Username> findByEmail(String email);
}

