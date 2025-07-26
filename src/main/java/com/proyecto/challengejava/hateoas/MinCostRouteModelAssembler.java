package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.CostPointsController;
import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.MinCostRouteResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constants.*;

@Component
public class MinCostRouteModelAssembler implements RepresentationModelAssembler<MinCostRouteResponse, MinCostRouteResponse> {

    @Override
    public MinCostRouteResponse toModel(MinCostRouteResponse response) {
        if (response.getRute().size() >= 2) {
            Long origen = response.getRute().get(0);
            Long destino = response.getRute().get(response.getRute().size() - 1);
            CostPointsRequest request = new CostPointsRequest(origen, destino);

            response.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CostPointsController.class)
                            .calculateMinCost(request)
            ).withRel(RECALCULAR_RUTA));
        }

        response.getRute().forEach(id ->
                response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostPointsController.class)
                                .getCostsFromPoint(id)
                ).withRel(VER_COSTOS_DESDE + id))
        );

        return response;
    }
}
