package com.proyecto.challengejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AccreditationRequest {

    @NotNull(message = "The amount cannot be null")
    @Min(value = 0, message = "The amount cannot be negative")
    private Double amount;
    @NotNull(message = "The ID cannot be null")
    @Min(value = 1, message = "The ID cannot be more than 0")
    private Long idPointSale;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Long getIdPointSale() { return idPointSale; }
    public void setIdPointSale(Long idPointSale) { this.idPointSale = idPointSale; }
}
