package com.proyecto.challengejava.mapper;

import com.proyecto.challengejava.dto.AccreditationResponse;
import com.proyecto.challengejava.entity.Accreditation;

public class AccreditationMapper {
    public static AccreditationResponse mapToResponse(Accreditation entity) {
        return new AccreditationResponse(
                entity.getId(),
                entity.getAmount(),
                entity.getIdPointSale(),
                entity.getPointSaleName(),
                entity.getDateReception()
        );
    }
}

