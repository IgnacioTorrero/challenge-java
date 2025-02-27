package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_NOT_FOUND;

@Service
public class AcreditacionServiceImpl implements AcreditacionService {

    private final AcreditacionRepository repository;
    private final PuntoVentaServiceImpl puntoVentaServiceImpl;

    public AcreditacionServiceImpl(AcreditacionRepository repository, PuntoVentaServiceImpl puntoVentaServiceImpl) {
        this.repository = repository;
        this.puntoVentaServiceImpl = puntoVentaServiceImpl;
    }

    public Acreditacion recibirAcreditacion(Double importe, Long idPuntoVenta) {
        if (!puntoVentaExists(idPuntoVenta)) {
            throw new PuntoVentaNotFoundException(PUNTO_VENTA_NOT_FOUND + ": " + idPuntoVenta);
        }

        String nombrePuntoVenta = puntoVentaServiceImpl.getAllPuntosVenta().stream()
                .filter(p -> p.getId().equals(idPuntoVenta))
                .map(PuntoVenta::getNombre)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND));

        Acreditacion acreditacion = new Acreditacion();
        acreditacion.setImporte(importe);
        acreditacion.setIdPuntoVenta(idPuntoVenta);
        acreditacion.setNombrePuntoVenta(nombrePuntoVenta);
        acreditacion.setFechaRecepcion(LocalDate.now());

        return repository.save(acreditacion);
    }

    private boolean puntoVentaExists(Long id) {
        return puntoVentaServiceImpl.getAllPuntosVenta().stream().anyMatch(p -> p.getId().equals(id));
    }

    public Iterable<Acreditacion> obtenerAcreditaciones() {
        return repository.findAll();
    }
}