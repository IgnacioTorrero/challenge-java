package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostPointsResponse;
import java.util.List;

public interface CostPointsService {
    void addCostPoints(Long idA, Long idB, Double cost);
    void removeCostPoints(Long idA, Long idB);
    List<CostPointsResponse> getCostsFromPoint(Long idA);
    List<Long> calculateMinPath(Long pointA, Long pointB);
    Double calculateTotalRouteCost(List<Long> rute);
    void deleteRelatedCostsTo(Long id);
}
