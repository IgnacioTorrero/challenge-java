package com.proyecto.challengejava.mapper;

import com.proyecto.challengejava.dto.AccreditationResponse;
import com.proyecto.challengejava.entity.Acreditacion;

public class AcreditacionMapper {
    public static AccreditationResponse mapToResponse(Acreditacion entity) {
        return new AccreditationResponse(
                entity.getId(),
                entity.getImporte(),
                entity.getIdPuntoVenta(),
                entity.getNombrePuntoVenta(),
                entity.getFechaRecepcion()
        );
    }
}

