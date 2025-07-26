package com.proyecto.challengejava.mapper;

import com.proyecto.challengejava.dto.AccreditationResponse;
import com.proyecto.challengejava.entity.Accreditation;

public class AccreditationMapper {
    public static AccreditationResponse mapToResponse(Accreditation entity) {
        return new AccreditationResponse(
                entity.getId(),
                entity.getImporte(),
                entity.getIdPuntoVenta(),
                entity.getNombrePuntoVenta(),
                entity.getFechaRecepcion()
        );
    }
}

