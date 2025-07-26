package com.proyecto.challengejava.service;

import org.springframework.stereotype.Service;

@Service
public class PointSaleManager {

    private final PointSaleService pointSaleService;
    private final CostPointsService costPointsService;

    public PointSaleManager(PointSaleService pointSaleService,
                            CostPointsService costPointsService) {
        this.pointSaleService = pointSaleService;
        this.costPointsService = costPointsService;
    }

    /**
     * Deletes a sales point along with all costs associated with it.
     *
     * @param id ID of the sales point to be deleted.
     */
    public void eliminarPuntoVentaConCostos(Long id) {
        costPointsService.eliminarCostosRelacionadosA(id);
        pointSaleService.deletePuntoVenta(id);
    }
}