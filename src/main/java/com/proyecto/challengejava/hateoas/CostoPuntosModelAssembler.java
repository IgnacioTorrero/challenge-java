package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.CostoPuntosController;
import com.proyecto.challengejava.dto.CostoPuntosRequest;
import com.proyecto.challengejava.dto.CostoPuntosResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CostoPuntosModelAssembler implements RepresentationModelAssembler<CostoPuntosResponse, CostoPuntosResponse> {

    @Override
    public CostoPuntosResponse toModel(CostoPuntosResponse entity) {
        entity.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostoPuntosController.class)
                                .getCostosDesdePunto(entity.getIdB())
                ).withRel("ver-costos-desde-este-punto")
        );

        entity.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostoPuntosController.class)
                                .calcularCostoMinimo(new CostoPuntosRequest(entity.getIdA(), entity.getIdB()))
                ).withRel("calcular-ruta-minima")
        );

        return entity;
    }
}
