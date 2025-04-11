package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.CostoPuntosController;
import com.proyecto.challengejava.dto.CostoPuntosRequest;
import com.proyecto.challengejava.dto.RutaCostoMinimoResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class RutaCostoMinimoModelAssembler implements RepresentationModelAssembler<RutaCostoMinimoResponse, RutaCostoMinimoResponse> {

    @Override
    public RutaCostoMinimoResponse toModel(RutaCostoMinimoResponse response) {
        if (response.getRuta().size() >= 2) {
            Long origen = response.getRuta().get(0);
            Long destino = response.getRuta().get(response.getRuta().size() - 1);
            CostoPuntosRequest request = new CostoPuntosRequest(origen, destino);

            response.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CostoPuntosController.class)
                            .calcularCostoMinimo(request)
            ).withRel("recalcular-ruta"));
        }

        response.getRuta().forEach(id ->
                response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CostoPuntosController.class)
                                .getCostosDesdePunto(id)
                ).withRel("ver-costos-desde-" + id))
        );

        return response;
    }
}
