package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.PuntoVentaController;
import com.proyecto.challengejava.dto.PuntoVentaResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constantes.*;

@Component
public class PuntoVentaModelAssembler implements RepresentationModelAssembler<PuntoVentaResponse, PuntoVentaResponse> {

    @Override
    public PuntoVentaResponse toModel(PuntoVentaResponse puntoVenta) {
        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                                .getAllPuntosVenta()
                ).withRel(LISTAR_TODOS)
        );

        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                                .deletePuntoVenta(puntoVenta.getId())
                ).withRel(ELIMINAR)
        );

        return puntoVenta;
    }
}
