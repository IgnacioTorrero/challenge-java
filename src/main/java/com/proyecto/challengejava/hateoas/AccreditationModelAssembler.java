package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.AccreditationController;
import com.proyecto.challengejava.controller.PointSaleController;
import com.proyecto.challengejava.dto.AccreditationResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constants.*;

@Component
public class AccreditationModelAssembler implements RepresentationModelAssembler<AccreditationResponse, AccreditationResponse> {

    @Override
    public AccreditationResponse toModel(AccreditationResponse entity) {
        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PointSaleController.class)
                        .getAllPointsSale()
        ).withRel(VER_TODOS_PUNTOS_DE_VENTA));

        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AccreditationController.class)
                        .getAccreditations()
        ).withRel(VER_TODAS_ACREDITACIONES));

        return entity;
    }
}