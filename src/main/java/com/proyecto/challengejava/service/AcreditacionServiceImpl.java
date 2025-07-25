package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constantes.*;

@Service
public class AcreditacionServiceImpl implements AcreditacionService {

    private final AcreditacionRepository repository;
    private final PuntoVentaService puntoVentaService;

    public AcreditacionServiceImpl(AcreditacionRepository repository, PuntoVentaService puntoVentaService) {
        this.repository = repository;
        this.puntoVentaService = puntoVentaService;
    }

    public Acreditacion recibirAcreditacion(Double importe, Long idPuntoVenta) {
        if (!puntoVentaExists(idPuntoVenta)) {
            throw new PuntoVentaNotFoundException(PUNTO_VENTA_NOT_FOUND + ": " + idPuntoVenta);
        }

        String nombrePuntoVenta = puntoVentaService.getAllPuntosVenta().stream()
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
        return puntoVentaService.getAllPuntosVenta().stream().anyMatch(p -> p.getId().equals(id));
    }

    public Iterable<Acreditacion> obtenerAcreditaciones() {
        return repository.findAll();
    }
}