package com.proyecto.challengejava.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Accreditation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Long idPointSale;
    private String pointSaleName;
    private LocalDate dateReception;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getIdPointSale() {
        return idPointSale;
    }

    public void setIdPointSale(Long idPointSale) {
        this.idPointSale = idPointSale;
    }

    public String getPointSaleName() {
        return pointSaleName;
    }

    public void setPointSaleName(String pointSaleName) {
        this.pointSaleName = pointSaleName;
    }

    public LocalDate getDateReception() {
        return dateReception;
    }

    public void setDateReception(LocalDate dateReception) {
        this.dateReception = dateReception;
    }
}
