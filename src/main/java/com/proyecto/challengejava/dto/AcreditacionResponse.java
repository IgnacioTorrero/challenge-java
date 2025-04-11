package com.proyecto.challengejava.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class AcreditacionResponse extends RepresentationModel<AcreditacionResponse> {
    private Long id;
    private Double importe;
    private Long idPuntoVenta;
    private String nombrePuntoVenta;
    private LocalDate fechaRecepcion;

    public AcreditacionResponse(Long id, Double importe, Long idPuntoVenta, String nombrePuntoVenta, LocalDate fechaRecepcion) {
        this.id = id;
        this.importe = importe;
        this.idPuntoVenta = idPuntoVenta;
        this.nombrePuntoVenta = nombrePuntoVenta;
        this.fechaRecepcion = fechaRecepcion;
    }

    public Long getId() { return id; }
    public Double getImporte() { return importe; }
    public Long getIdPuntoVenta() { return idPuntoVenta; }
    public String getNombrePuntoVenta() { return nombrePuntoVenta; }
    public LocalDate getFechaRecepcion() { return fechaRecepcion; }
}
