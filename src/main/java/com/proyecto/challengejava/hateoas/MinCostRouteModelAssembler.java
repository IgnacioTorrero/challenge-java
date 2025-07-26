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
        if (response.getRoute().size() >= 2) {
            Long origin = response.getRoute().get(0);
            Long destination = response.getRoute().get(response.getRoute().size() - 1);
            CostPointsRequest request = new CostPointsRequest(origin, destination);

            response.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CostPointsController.class)
                            .calculateMinCost(request)
            ).withRel(RECALCULATE_ROUTE));
        }

        response.getRoute().forEach(id ->
                response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostPointsController.class)
                                .getCostsFromPoint(id)
                ).withRel(SEE_COSTS_FROM + id))
        );

        return response;
    }
}
