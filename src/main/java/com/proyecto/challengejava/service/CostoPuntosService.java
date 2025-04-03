package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostoPuntosResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import java.util.List;

public interface CostoPuntosService {
    void addCostoPuntos(Long idA, Long idB, Double costo);
    void removeCostoPuntos(Long idA, Long idB);
    List<CostoPuntosResponse> getCostosDesdePunto(Long idA);
    List<Long> calcularRutaMinima(Long puntoA, Long puntoB);
    Double calcularCostoTotalRuta(List<Long> ruta);
}
