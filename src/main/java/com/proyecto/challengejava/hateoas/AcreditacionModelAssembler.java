package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.AcreditacionController;
import com.proyecto.challengejava.controller.PuntoVentaController;
import com.proyecto.challengejava.dto.AcreditacionResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AcreditacionModelAssembler implements RepresentationModelAssembler<AcreditacionResponse, AcreditacionResponse> {

    @Override
    public AcreditacionResponse toModel(AcreditacionResponse entity) {
        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PuntoVentaController.class)
                        .getAllPuntosVenta()
        ).withRel("ver-todos-los-puntos-venta"));

        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AcreditacionController.class)
                        .obtenerAcreditaciones()
        ).withRel("ver-todas-las-acreditaciones"));

        return entity;
    }
}