package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.PuntoVentaController;
import com.proyecto.challengejava.dto.PuntoVentaResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class PuntoVentaModelAssembler implements RepresentationModelAssembler<PuntoVentaResponse, PuntoVentaResponse> {

    @Override
    public PuntoVentaResponse toModel(PuntoVentaResponse puntoVenta) {
        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                                .getAllPuntosVenta()
                ).withRel("listar-todos")
        );

        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                                .deletePuntoVenta(puntoVenta.getId())
                ).withRel("eliminar")
        );

        return puntoVenta;
    }
}
