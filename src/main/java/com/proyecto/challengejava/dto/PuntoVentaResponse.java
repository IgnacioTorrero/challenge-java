package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

public class PuntoVentaResponse extends RepresentationModel<PuntoVentaResponse> {
    private Long id;
    private String nombre;

    public PuntoVentaResponse(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
