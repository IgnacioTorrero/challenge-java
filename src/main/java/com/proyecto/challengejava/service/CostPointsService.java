package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostPointsResponse;
import java.util.List;

public interface CostPointsService {
    void addCostoPuntos(Long idA, Long idB, Double costo);
    void removeCostoPuntos(Long idA, Long idB);
    List<CostPointsResponse> getCostosDesdePunto(Long idA);
    List<Long> calcularRutaMinima(Long puntoA, Long puntoB);
    Double calcularCostoTotalRuta(List<Long> ruta);
    void eliminarCostosRelacionadosA(Long id);
}
