package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constantes.*;

/**
 * Implementación del servicio de acreditaciones.
 * Encargado de registrar nuevas acreditaciones y consultar las existentes.
 */
@Service
public class AcreditacionServiceImpl implements AcreditacionService {

    private final AcreditacionRepository repository;
    private final PuntoVentaService puntoVentaService;

    /**
     * Constructor que inyecta los repositorios y servicios requeridos.
     *
     * @param repository          Repositorio de acreditaciones.
     * @param puntoVentaService   Servicio para gestionar puntos de venta.
     */
    public AcreditacionServiceImpl(AcreditacionRepository repository, PuntoVentaService puntoVentaService) {
        this.repository = repository;
        this.puntoVentaService = puntoVentaService;
    }

    /**
     * Registra una nueva acreditación para un punto de venta específico.
     *
     * @param importe        Importe recibido.
     * @param idPuntoVenta   ID del punto de venta que recibe la acreditación.
     * @return Objeto {@link Acreditacion} persistido en la base de datos.
     * @throws PuntoVentaNotFoundException si el punto de venta no existe.
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
     * Verifica si existe un punto de venta con el ID especificado.
     *
     * @param id ID del punto de venta a verificar.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    private boolean puntoVentaExists(Long id) {
        return puntoVentaService.getAllPuntosVenta().stream().anyMatch(p -> p.getId().equals(id));
    }

    /**
     * Obtiene todas las acreditaciones almacenadas en la base de datos.
     *
     * @return Iterable de objetos {@link Acreditacion}.
     */
    public Iterable<Acreditacion> obtenerAcreditaciones() {
        return repository.findAll();
    }
}