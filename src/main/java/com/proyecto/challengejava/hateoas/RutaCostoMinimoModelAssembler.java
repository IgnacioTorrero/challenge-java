package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.CostPointsController;
import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.MinCostRouteResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constants.*;

@Component
public class RutaCostoMinimoModelAssembler implements RepresentationModelAssembler<MinCostRouteResponse, MinCostRouteResponse> {

    @Override
    public MinCostRouteResponse toModel(MinCostRouteResponse response) {
        if (response.getRuta().size() >= 2) {
            Long origen = response.getRuta().get(0);
            Long destino = response.getRuta().get(response.getRuta().size() - 1);
            CostPointsRequest request = new CostPointsRequest(origen, destino);

            response.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CostPointsController.class)
                            .calcularCostoMinimo(request)
            ).withRel(RECALCULAR_RUTA));
        }

        response.getRuta().forEach(id ->
                response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostPointsController.class)
                                .getCostosDesdePunto(id)
                ).withRel(VER_COSTOS_DESDE + id))
        );

        return response;
    }
}
