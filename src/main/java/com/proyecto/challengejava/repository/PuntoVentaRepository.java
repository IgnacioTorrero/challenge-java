package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.PuntoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntoVentaRepository extends JpaRepository<PuntoVenta, Long> {
    boolean existsById(Long id);
    boolean existsByNombre(String nombre);
}