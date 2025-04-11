package com.proyecto.challengejava.mapper;

import com.proyecto.challengejava.dto.AcreditacionResponse;
import com.proyecto.challengejava.entity.Acreditacion;

public class AcreditacionMapper {
    public static AcreditacionResponse mapToResponse(Acreditacion entity) {
        return new AcreditacionResponse(
                entity.getId(),
                entity.getImporte(),
                entity.getIdPuntoVenta(),
                entity.getNombrePuntoVenta(),
                entity.getFechaRecepcion()
        );
    }
}

