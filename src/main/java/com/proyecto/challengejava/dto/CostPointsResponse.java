package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

public class CostPointsResponse extends RepresentationModel<CostPointsResponse> {
    private Long idA;
    private Long idB;
    private Double cost;
    private String pointNameB;

    public CostPointsResponse(Long idA, Long idB, Double cost, String pointNameB) {
        this.idA = idA;
        this.idB = idB;
        this.cost = cost;
        this.pointNameB = pointNameB;
    }

    // Getters y setters
    public Long getIdA() { return idA; }
    public void setIdA(Long idA) { this.idA = idA; }

    public Long getIdB() { return idB; }
    public void setIdB(Long idB) { this.idB = idB; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getPointNameB() { return pointNameB; }
    public void setPointNameB(String pointNameB) { this.pointNameB = pointNameB; }
}
