package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.CostoPuntos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostoRepository extends JpaRepository<CostoPuntos, Long> {
    Optional<CostoPuntos> findByIdAAndIdB(Long idA, Long idB);
}