package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Accreditation;

public interface AcreditacionService {
    Accreditation recibirAcreditacion(Double importe, Long idPuntoVenta);
    Iterable<Accreditation> obtenerAcreditaciones();
}
