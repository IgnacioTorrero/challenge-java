package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constants.*;

/**
 * Implementation of the accreditation service.
 * Responsible for registering new accreditations and querying existing ones.
 */
@Service
public class AcreditacionServiceImpl implements AcreditacionService {

    private final AcreditacionRepository repository;
    private final PuntoVentaService puntoVentaService;

    /**
     * Constructor that injects the required repositories and services.
     *
     * @param repository        Repository for accreditations.
     * @param puntoVentaService Service for managing sales points.
     */
    public AcreditacionServiceImpl(AcreditacionRepository repository, PuntoVentaService puntoVentaService) {
        this.repository = repository;
        this.puntoVentaService = puntoVentaService;
    }

    /**
     * Registers a new accreditation for a specific sales point.
     *
     * @param importe        Amount received.
     * @param idPuntoVenta   ID of the sales point receiving the accreditation.
     * @return {@link Acreditacion} object persisted in the database.
     * @throws PuntoVentaNotFoundException if the sales point does not exist.
     */
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

    /**
     * Checks whether a sales point with the specified ID exists.
     *
     * @param id ID of the sales point to verify.
     * @return {@code true} if it exists, {@code false} otherwise.
     */
    private boolean puntoVentaExists(Long id) {
        return puntoVentaService.getAllPuntosVenta().stream().anyMatch(p -> p.getId().equals(id));
    }

    /**
     * Retrieves all accreditations stored in the database.
     *
     * @return Iterable of {@link Acreditacion} objects.
     */
    public Iterable<Acreditacion> obtenerAcreditaciones() {
        return repository.findAll();
    }
}