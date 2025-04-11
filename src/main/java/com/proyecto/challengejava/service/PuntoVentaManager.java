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

    public void eliminarPuntoVentaConCostos(Long id) {
        costoPuntosService.eliminarCostosRelacionadosA(id);
        puntoVentaService.deletePuntoVenta(id);
    }
}
