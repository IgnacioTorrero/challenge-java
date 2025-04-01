package com.proyecto.challengejava.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CostoPuntos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idA;
    private Long idB;
    private Double costo;
    private String nombrePuntoB;

    public CostoPuntos() {
    }

    public CostoPuntos(Long idA, Long idB, Double costo, String nombrePuntoB) {
        this.idA = idA;
        this.idB = idB;
        this.costo = costo;
        this.nombrePuntoB = nombrePuntoB;
    }

    public Long getIdA() {
        return idA;
    }

    public void setIdA(Long idA) {
        this.idA = idA;
    }

    public Long getIdB() {
        return idB;
    }

    public void setIdB(Long idB) {
        this.idB = idB;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public String getNombrePuntoB() {
        return nombrePuntoB;
    }

    public void setNombrePuntoB(String nombreIdB) {
        this.nombrePuntoB = nombreIdB;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
