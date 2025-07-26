package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class MinCostRouteResponse extends RepresentationModel<MinCostRouteResponse> {
    private List<Long> ruta;
    private Double costoTotal;

    public MinCostRouteResponse(List<Long> ruta, Double costoTotal) {
        this.ruta = ruta;
        this.costoTotal = costoTotal;
    }

    public List<Long> getRuta() {
        return ruta;
    }

    public void setRuta(List<Long> ruta) {
        this.ruta = ruta;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }
}
