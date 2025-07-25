package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.AcreditacionController;
import com.proyecto.challengejava.controller.PuntoVentaController;
import com.proyecto.challengejava.dto.AcreditacionResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constantes.*;

@Component
public class AcreditacionModelAssembler implements RepresentationModelAssembler<AcreditacionResponse, AcreditacionResponse> {

    @Override
    public AcreditacionResponse toModel(AcreditacionResponse entity) {
        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                        .getAllPuntosVenta()
        ).withRel(VER_TODOS_PUNTOS_DE_VENTA));

        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AcreditacionController.class)
                        .obtenerAcreditaciones()
        ).withRel(VER_TODAS_ACREDITACIONES));

        return entity;
    }
}