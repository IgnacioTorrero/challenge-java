package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import java.util.List;

public interface PuntoVentaService {
    List<PuntoVenta> getAllPuntosVenta();
    void addPuntoVenta(Long id, String nombre);
    void updatePuntoVenta(Long id, String nombre);
    void deletePuntoVenta(Long id);
}
