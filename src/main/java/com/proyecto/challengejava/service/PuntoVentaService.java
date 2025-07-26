package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PointSale;
import java.util.List;

public interface PuntoVentaService {
    List<PointSale> getAllPuntosVenta();
    void addPuntoVenta(String nombre);
    void updatePuntoVenta(Long id, String nombre);
    void deletePuntoVenta(Long id);
}
