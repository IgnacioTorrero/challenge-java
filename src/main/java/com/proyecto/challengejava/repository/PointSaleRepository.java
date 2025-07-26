package com.proyecto.challengejava.repository;

import com.proyecto.challengejava.entity.PointSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointSaleRepository extends JpaRepository<PointSale, Long> {
    boolean existsById(Long id);
    boolean existsByNombre(String nombre);
}