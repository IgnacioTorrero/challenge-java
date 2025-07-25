package com.proyecto.challengejava.service;

import org.springframework.stereotype.Service;

@Service
public class PuntoVentaManager {

    private final PuntoVentaService puntoVentaService;
    private final CostoPuntosService costoPuntosService;

    public PuntoVentaManager(PuntoVentaService puntoVentaService,
                             CostoPuntosService costoPuntosService) {
        this.puntoVentaService = puntoVentaService;
        this.costoPuntosService = costoPuntosService;
    }

    /**
     * Deletes a sales point along with all costs associated with it.
     *
     * @param id ID of the sales point to be deleted.
     */
    public void eliminarPuntoVentaConCostos(Long id) {
        costoPuntosService.eliminarCostosRelacionadosA(id);
        puntoVentaService.deletePuntoVenta(id);
    }
}