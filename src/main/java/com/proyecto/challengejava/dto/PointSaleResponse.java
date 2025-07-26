package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

public class PointSaleResponse extends RepresentationModel<PointSaleResponse> {
    private Long id;
    private String nombre;

    public PointSaleResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
