package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class MinCostRouteResponse extends RepresentationModel<MinCostRouteResponse> {
    private List<Long> rute;
    private Double totalCost;

    public MinCostRouteResponse(List<Long> rute, Double totalCost) {
        this.rute = rute;
        this.totalCost = totalCost;
    }

    public List<Long> getRute() {
        return rute;
    }

    public void setRute(List<Long> rute) {
        this.rute = rute;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
