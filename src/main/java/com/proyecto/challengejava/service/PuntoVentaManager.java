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
     * Elimina un punto de venta junto con todos los costos asociados a él.
     *
     * @param id ID del punto de venta a eliminar.
     */
    public void eliminarPuntoVentaConCostos(Long id) {
        costoPuntosService.eliminarCostosRelacionadosA(id);
        puntoVentaService.deletePuntoVenta(id);
    }
}