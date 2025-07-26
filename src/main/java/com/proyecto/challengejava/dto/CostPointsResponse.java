package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

public class CostPointsResponse extends RepresentationModel<CostPointsResponse> {
    private Long idA;
    private Long idB;
    private Double costo;
    private String nombrePuntoB;

    public CostPointsResponse(Long idA, Long idB, Double costo, String nombrePuntoB) {
        this.idA = idA;
        this.idB = idB;
        this.costo = costo;
        this.nombrePuntoB = nombrePuntoB;
    }

    // Getters y setters
    public Long getIdA() { return idA; }
    public void setIdA(Long idA) { this.idA = idA; }

    public Long getIdB() { return idB; }
    public void setIdB(Long idB) { this.idB = idB; }

    public Double getCosto() { return costo; }
    public void setCosto(Double costo) { this.costo = costo; }

    public String getNombrePuntoB() { return nombrePuntoB; }
    public void setNombrePuntoB(String nombrePuntoB) { this.nombrePuntoB = nombrePuntoB; }
}
