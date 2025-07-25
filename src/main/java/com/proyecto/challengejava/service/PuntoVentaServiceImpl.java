package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.PuntoVentaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constants.*;

/**
 * Implementation of the sales point service.
 * Manages CRUD operations and an in-memory cache.
 */
@Service
public class PuntoVentaServiceImpl implements PuntoVentaService {

    private final Map<Long, String> cache = new ConcurrentHashMap<>();
    private final PuntoVentaRepository puntoVentaRepository;

    @Autowired
    private Environment env;

    /**
     * Constructor that injects the sales point repository.
     *
     * @param puntoVentaRepository JPA repository for persisting sales points.
     */
    public PuntoVentaServiceImpl(PuntoVentaRepository puntoVentaRepository) {
        this.puntoVentaRepository = puntoVentaRepository;
    }

    /**
     * Initializes the cache unless running in the test environment.
     * Automatically executed after the bean is constructed.
     */
    @PostConstruct
    public void init() {
//        precargarPuntosVentaSiNoExisten();
        if (!Arrays.asList(env.getActiveProfiles()).contains("test")) {
            precargarCache();
        }
    }

    /*
     * Alternative method to load cache from the database.
     * Currently unused.
     */
//    private void cargarCacheDesdeDB() {
//        puntoVentaRepository.findAll().forEach(p -> cache.put(p.getId(), p.getNombre()));
//    }

    /*
     * Preloads sales points into the database if they do not exist.
     * Commented out because the containerized database is used instead.
     */
//    private void precargarPuntosVentaSiNoExisten() {
//        if (puntoVentaRepository.count() == 0) {
//            List<PuntoVenta> puntos = new ArrayList<>();
//            for (long i = 1; i <= 10; i++) {
//                PuntoVenta puntoVenta = new PuntoVenta();
//                puntoVenta.setNombre(PUNTOS_VENTA.get((int) (i - 1)));
//                puntos.add(puntoVenta);
//            }
//            puntoVentaRepository.saveAll(puntos);
//        }
//    }

    /**
     * Retrieves all sales points registered in the database.
     *
     * @return List of {@link PuntoVenta} entities.
     */
    public List<PuntoVenta> getAllPuntosVenta() {
        return puntoVentaRepository.findAll();
    }

    /**
     * Preloads the in-memory cache with all sales points from the database.
     */
    private void precargarCache() {
        List<PuntoVenta> puntos = puntoVentaRepository.findAll();
        for (PuntoVenta p : puntos) {
            cache.put(p.getId(), p.getNombre());
        }
    }

    /**
     * Adds a new sales point if no other exists with the same name.
     *
     * @param nombre Name of the sales point to create.
     * @throws IllegalArgumentException if a point with the same name already exists.
     */
    @Override
    public void addPuntoVenta(String nombre) {
        if (puntoVentaRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException(PUNTO_VENTA_ALREADY_EXISTS);
        }
        PuntoVenta puntoVenta = new PuntoVenta();
        puntoVenta.setNombre(nombre);
        puntoVenta = puntoVentaRepository.save(puntoVenta);
        cache.put(puntoVenta.getId(), nombre);
    }

    /**
     * Updates the name of an existing sales point.
     *
     * @param id     ID of the sales point to update.
     * @param nombre New name for the sales point.
     * @throws IllegalArgumentException if the ID does not correspond to an existing point.
     */
    public void updatePuntoVenta(Long id, String nombre) {
        PuntoVenta puntoVenta = puntoVentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND));
        puntoVenta.setNombre(nombre);
        puntoVentaRepository.save(puntoVenta);
        cache.put(id, nombre);
    }

    /**
     * Deletes a sales point from both the database and the cache.
     *
     * @param id ID of the sales point to delete.
     * @throws IllegalArgumentException if the ID does not exist in the database.
     */
    public void deletePuntoVenta(Long id) {
        if (!puntoVentaRepository.existsById(id)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        puntoVentaRepository.deleteById(id);
        cache.remove(id);
    }
}