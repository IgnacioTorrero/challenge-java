package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.CostPointsController;
import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.CostPointsResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constants.*;

@Component
public class CostPointsModelAssembler implements RepresentationModelAssembler<CostPointsResponse, CostPointsResponse> {

    @Override
    public CostPointsResponse toModel(CostPointsResponse entity) {
        entity.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostPointsController.class)
                                .getCostsFromPoint(entity.getIdB())
                ).withRel(VER_COSTOS_DESDE_PUNTO)
        );

        entity.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostPointsController.class)
                                .calculateMinCost(new CostPointsRequest(entity.getIdA(), entity.getIdB()))
                ).withRel(CALCULAR_RUTA_MINIMA)
        );

        return entity;
    }
}
