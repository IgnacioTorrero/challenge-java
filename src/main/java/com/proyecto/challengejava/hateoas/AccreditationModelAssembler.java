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
        ).withRel(SEE_ALL_POINTS_OF_SALE));

        entity.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AccreditationController.class)
                        .getAccreditations()
        ).withRel(SEE_ALL_ACCREDITATIONS));

        return entity;
    }
}