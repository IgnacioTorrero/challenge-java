package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.PuntoVentaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constantes.*;

@Service
public class PuntoVentaServiceImpl implements PuntoVentaService {

    private final Map<Long, String> cache = new ConcurrentHashMap<>();
    private final PuntoVentaRepository puntoVentaRepository;

    public PuntoVentaServiceImpl(PuntoVentaRepository puntoVentaRepository) {
        this.puntoVentaRepository = puntoVentaRepository;
        precargarCache();
    }

    @PostConstruct
    public void init() {
        precargarPuntosVentaSiNoExisten();
        cargarCacheDesdeDB();
    }

    private void cargarCacheDesdeDB() {
        puntoVentaRepository.findAll().forEach(p -> cache.put(p.getId(), p.getNombre()));
    }

    private void precargarPuntosVentaSiNoExisten() {
        if (puntoVentaRepository.count() == 0) {
            List<PuntoVenta> puntos = new ArrayList<>();
            for (long i = 1; i <= 10; i++) {
                PuntoVenta puntoVenta = new PuntoVenta();
                puntoVenta.setNombre(PUNTOS_VENTA.get((int) (i - 1)));
                puntoVenta.setNombre(PUNTOS_VENTA.get((int) (i - 1)));
                puntos.add(puntoVenta);
            }
            puntoVentaRepository.saveAll(puntos); // Solo si no había nada en la tabla
        }
    }

    public List<PuntoVenta> getAllPuntosVenta() {
        return puntoVentaRepository.findAll();
    }

    private void precargarCache() {
        List<PuntoVenta> puntos = puntoVentaRepository.findAll();
        for (PuntoVenta p : puntos) {
            cache.put(p.getId(), p.getNombre());
        }
    }

    public void addPuntoVenta(Long id, String nombre) {
        if (cache.containsKey(id)) {
            throw new IllegalArgumentException(PUNTO_VENTA_ALREADY_EXISTS);
        }
        PuntoVenta puntoVenta = new PuntoVenta();
        puntoVenta.setId(id);
        puntoVenta.setNombre(nombre);
        puntoVentaRepository.save(puntoVenta);
        cache.put(id, nombre);
    }

    public void updatePuntoVenta(Long id, String nombre) {
        PuntoVenta puntoVenta = puntoVentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND));
        puntoVenta.setNombre(nombre);
        puntoVentaRepository.save(puntoVenta);
        cache.put(id, nombre);
    }

    public void deletePuntoVenta(Long id) {
        if (!puntoVentaRepository.existsById(id)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        puntoVentaRepository.deleteById(id);
        cache.remove(id);
    }
}
