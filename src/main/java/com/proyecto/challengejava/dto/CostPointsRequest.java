package com.proyecto.challengejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CostPointsRequest {

    @NotNull(message = "The ID cannot be null")
    @Min(value = 1, message = "The ID cannot be more than 0")
    private Long idA;
    @NotNull(message = "The ID cannot be null")
    @Min(value = 1, message = "The ID cannot be more than 0")
    private Long idB;

    public CostPointsRequest(Long idA, Long idB) {
        this.idA = idA;
        this.idB = idB;
    }

    public Long getIdA() { return idA; }
    public void setIdA(Long id) { this.idA = id; }
    public Long getIdB() { return idB; }
    public void setIdB(Long id) { this.idB = id; }
}
