package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constantes.*;

@Service
public class CostoPuntosService {

    private final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
    private final PuntoVentaService puntoVentaService;

    @Autowired
    public CostoPuntosService(PuntoVentaService puntoVentaService) {
        this.puntoVentaService = puntoVentaService;
        cargarCostosIniciales();
    }

    public void cargarCostosIniciales() {
        agregarCostoInicial(1L, 2L, 2.0);
        agregarCostoInicial(1L, 3L, 3.0);
        agregarCostoInicial(2L, 3L, 5.0);
        agregarCostoInicial(2L, 4L, 10.0);
        agregarCostoInicial(1L, 4L, 11.0);
        agregarCostoInicial(4L, 5L, 5.0);
        agregarCostoInicial(2L, 5L, 14.0);
        agregarCostoInicial(6L, 7L, 32.0);
        agregarCostoInicial(8L, 9L, 11.0);
        agregarCostoInicial(10L, 7L, 5.0);
        agregarCostoInicial(3L, 8L, 10.0);
        agregarCostoInicial(5L, 8L, 30.0);
        agregarCostoInicial(10L, 5L, 5.0);
        agregarCostoInicial(4L, 6L, 6.0);
    }

    private void agregarCostoInicial(Long idA, Long idB, Double costo) {
        if (!puntoVentaExists(idA) || !puntoVentaExists(idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND + ": " + idA + " - " + idB);
        }
        cache.put(generateKey(idA, idB), costo);
        cache.put(generateKey(idB, idA), costo);
    }

    public void addCostoPuntos(Long idA, Long idB, Double costo) {
        if (costo < 0) {
            throw new IllegalArgumentException(COSTO_PUNTOS_LESS_THAN_ZERO);
        }
        if (!puntoVentaExists(idA) || !puntoVentaExists(idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        cache.put(generateKey(idA, idB), costo);
        cache.put(generateKey(idB, idA), costo);
    }

    public void removeCostoPuntos(Long idA, Long idB) {
        if (!puntoVentaExists(idA) || !puntoVentaExists(idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        String keyAB = generateKey(idA, idB);
        String keyBA = generateKey(idB, idA);

        if (cache.containsKey(keyAB)) {
            cache.put(keyAB, 0.0);
        }
        if (cache.containsKey(keyBA)) {
            cache.put(keyBA, 0.0);
        }
    }

    public List<CostoPuntos> getCostosDesdePunto(Long idA) {
        if (!puntoVentaExists(idA)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        List<CostoPuntos> costos = new ArrayList<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(REGEX);
            if (ids[0].equals(String.valueOf(idA))) {
                Long idB = Long.valueOf(ids[1]);
                String nombrePuntoB = puntoVentaService.getAllPuntosVenta().stream()
                        .filter(p -> p.getId().equals(idB))
                        .map(PuntoVenta::getNombre)
                        .findFirst()
                        .orElse(UNKNOWN);
                costos.add(new CostoPuntos(idA, idB, value, nombrePuntoB));
            }
        });
        return costos;
    }

    public List<Long> calcularRutaMinima(Long puntoA, Long puntoB) {
        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> predecesores = new HashMap<>();
        PriorityQueue<Map.Entry<Long, Double>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));

        puntoVentaService.getAllPuntosVenta().forEach(p -> distancias.put(p.getId(), Double.MAX_VALUE));
        distancias.put(puntoA, 0.0);

        pq.add(new AbstractMap.SimpleEntry<>(puntoA, 0.0));

        while (!pq.isEmpty()) {
            long actual = pq.poll().getKey();

            for (Map.Entry<Long, Double> vecino : getVecinos(actual).entrySet()) {
                double nuevoCosto = distancias.get(actual) + vecino.getValue();
                if (nuevoCosto < distancias.get(vecino.getKey())) {
                    distancias.put(vecino.getKey(), nuevoCosto);
                    predecesores.put(vecino.getKey(), actual);
                    pq.add(new AbstractMap.SimpleEntry<>(vecino.getKey(), nuevoCosto));
                }
            }
        }

        List<Long> ruta = new ArrayList<>();
        Long current = puntoB;
        while (current != null) {
            ruta.add(current);
            current = predecesores.get(current);
        }
        Collections.reverse(ruta);
        return ruta;
    }

    // Obtener puntos de venta conectados
    private Map<Long, Double> getVecinos(Long punto) {
        Map<Long, Double> vecinos = new HashMap<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split("-");
            if (Long.valueOf(ids[0]).equals(punto)) {
                vecinos.put(Long.valueOf(ids[1]), value);
            }
        });
        return vecinos;
    }

    private boolean puntoVentaExists(Long id) {
        return puntoVentaService.getAllPuntosVenta().stream().anyMatch(p -> p.getId().equals(id));
    }

    private String generateKey(Long idA, Long idB) {
        return idA + REGEX + idB;
    }
}
