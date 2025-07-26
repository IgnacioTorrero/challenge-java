package com.proyecto.challengejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AccreditationRequest {

    @NotNull(message = "El importe no puede ser nulo")
    @Min(value = 0, message = "El importe no puede ser negativo")
    private Double importe;
    @NotNull(message = "El ID no puede ser nulo")
    @Min(value = 1, message = "El ID debe ser mayor que 0")
    private Long idPuntoVenta;

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }
    public Long getIdPuntoVenta() { return idPuntoVenta; }
    public void setIdPuntoVenta(Long idPuntoVenta) { this.idPuntoVenta = idPuntoVenta; }
}
