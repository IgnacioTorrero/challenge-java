package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class MinCostRouteResponse extends RepresentationModel<MinCostRouteResponse> {
    private List<Long> route;
    private Double totalCost;

    public MinCostRouteResponse(List<Long> route, Double totalCost) {
        this.route = route;
        this.totalCost = totalCost;
    }

    public List<Long> getRoute() {
        return route;
    }

    public void setRoute(List<Long> route) {
        this.route = route;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
