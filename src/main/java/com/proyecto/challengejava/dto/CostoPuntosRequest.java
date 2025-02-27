package com.proyecto.challengejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CostoPuntosRequest {

    @NotNull(message = "El ID no puede ser nulo")
    @Min(value = 1, message = "El ID debe ser mayor que 0")
    private Long idA;
    @NotNull(message = "El ID no puede ser nulo")
    @Min(value = 1, message = "El ID debe ser mayor que 0")
    private Long idB;

    public Long getIdA() { return idA; }
    public void setIdA(Long id) { this.idA = id; }
    public Long getIdB() { return idB; }
    public void setIdB(Long id) { this.idB = id; }
}
