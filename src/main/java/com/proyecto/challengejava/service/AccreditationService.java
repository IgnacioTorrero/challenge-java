package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Accreditation;

public interface AccreditationService {
    Accreditation recibirAcreditacion(Double importe, Long idPuntoVenta);
    Iterable<Accreditation> obtenerAcreditaciones();
}
