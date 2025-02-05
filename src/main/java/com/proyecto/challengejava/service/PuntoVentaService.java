package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.proyecto.challengejava.constants.Constantes.PUNTOS_VENTA;
import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_NOT_FOUND;

@Service
public class PuntoVentaService {

    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public PuntoVentaService() {
        cache.put(1L, PUNTOS_VENTA.get(0));
        cache.put(2L, PUNTOS_VENTA.get(1));
        cache.put(3L, PUNTOS_VENTA.get(2));
        cache.put(4L, PUNTOS_VENTA.get(3));
        cache.put(5L, PUNTOS_VENTA.get(4));
        cache.put(6L, PUNTOS_VENTA.get(5));
        cache.put(7L, PUNTOS_VENTA.get(6));
        cache.put(8L, PUNTOS_VENTA.get(7));
        cache.put(9L, PUNTOS_VENTA.get(8));
        cache.put(10L, PUNTOS_VENTA.get(9));
    }

    public List<PuntoVenta> getAllPuntosVenta() {
        return cache.entrySet().stream()
                .map(entry -> {
                    PuntoVenta punto = new PuntoVenta();
                    punto.setId(entry.getKey());
                    punto.setNombre(entry.getValue());
                    return punto;
                })
                .collect(Collectors.toList());
    }

    public void addPuntoVenta(Long id, String nombre) {
        cache.put(id, nombre);
    }

    public void updatePuntoVenta(Long id, String nombre) {
        if (cache.containsKey(id)) {
            cache.put(id, nombre);
        } else {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
    }

    public void deletePuntoVenta(Long id) {
        cache.remove(id);
    }
}
