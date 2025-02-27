package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;

public interface AcreditacionService {
    Acreditacion recibirAcreditacion(Double importe, Long idPuntoVenta);
    Iterable<Acreditacion> obtenerAcreditaciones();
}
