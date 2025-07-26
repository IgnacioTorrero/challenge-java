package com.proyecto.challengejava.hateoas;

import com.proyecto.challengejava.controller.PointSaleController;
import com.proyecto.challengejava.dto.PointSaleResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static com.proyecto.challengejava.constants.Constants.*;

@Component
public class PointSaleModelAssembler implements RepresentationModelAssembler<PointSaleResponse, PointSaleResponse> {

    @Override
    public PointSaleResponse toModel(PointSaleResponse puntoVenta) {
        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PointSaleController.class)
                                .getAllPuntosVenta()
                ).withRel(LISTAR_TODOS)
        );

        puntoVenta.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PointSaleController.class)
                                .deletePuntoVenta(puntoVenta.getId())
                ).withRel(ELIMINAR)
        );

        return puntoVenta;
    }
}
