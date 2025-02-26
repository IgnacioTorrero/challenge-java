package com.proyecto.challengejava.dto;

import java.util.List;

public class RutaCostoMinimoResponse {
    private List<Long> ruta;
    private Double costoTotal;

    public RutaCostoMinimoResponse(List<Long> ruta, Double costoTotal) {
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
