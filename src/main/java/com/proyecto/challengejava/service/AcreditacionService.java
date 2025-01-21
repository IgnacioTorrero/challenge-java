package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_NOT_FOUND;

@Service
public class AcreditacionService {

    private final AcreditacionRepository repository;
    private final PuntoVentaService puntoVentaService;

    @Autowired
    public AcreditacionService(AcreditacionRepository repository, PuntoVentaService puntoVentaService) {
        this.repository = repository;
        this.puntoVentaService = puntoVentaService;
    }

    public Acreditacion recibirAcreditacion(Double importe, Long idPuntoVenta) {
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

    public Iterable<Acreditacion> obtenerAcreditaciones() {
        return repository.findAll();
    }
}