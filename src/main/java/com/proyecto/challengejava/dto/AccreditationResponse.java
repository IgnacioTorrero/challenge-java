package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class AccreditationResponse extends RepresentationModel<AccreditationResponse> {
    private Long id;
    private Double amount;
    private Long idPointSale;
    private String pointSaleName;
    private LocalDate dateReception;

    public AccreditationResponse(Long id, Double amount, Long idPointSale, String pointSaleName, LocalDate dateReception) {
        this.id = id;
        this.amount = amount;
        this.idPointSale = idPointSale;
        this.pointSaleName = pointSaleName;
        this.dateReception = dateReception;
    }

    public Long getId() { return id; }
    public Double getAmount() { return amount; }
    public Long getIdPointSale() { return idPointSale; }
    public String getPointSaleName() { return pointSaleName; }
    public LocalDate getDateReception() { return dateReception; }
}
