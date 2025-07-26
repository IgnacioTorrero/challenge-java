package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.CostPoints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostRepository extends JpaRepository<CostPoints, Long> {
    Optional<CostPoints> findByIdAAndIdB(Long idA, Long idB);
}