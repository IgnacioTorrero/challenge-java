package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostoPuntosResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.CostoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constantes.*;
import static com.proyecto.challengejava.util.CostoPuntosUtil.*;

@Service
public class CostoPuntosServiceImpl implements CostoPuntosService {

    private final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
    private final PuntoVentaService puntoVentaService;
    private final CostoRepository costoRepository;

    public CostoPuntosServiceImpl(PuntoVentaService puntoVentaService, CostoRepository costoRepository) {
        this.puntoVentaService = puntoVentaService;
        this.costoRepository = costoRepository;
    }

    @PostConstruct
    public void init() {
        // Esperar a que se hayan precargado los puntos de venta
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
//        if (costoRepository.count() == 0 && !puntos.isEmpty()) {
//            try {
//                precargarCostosIniciales();
//            } catch (IllegalArgumentException e) {
//                System.err.println("⚠️ No se precargaron costos: " + e.getMessage());
//            }
//        } else if (puntos.isEmpty()) {
//            System.out.println("⚠️ No hay puntos de venta disponibles aún, se omite la precarga de costos.");
//        }

        cargarCacheDesdeDB();
    }

    public void cargarCacheDesdeDB() {
        costoRepository.findAll().forEach(costo -> {
            Long idA = costo.getIdA();
            Long idB = costo.getIdB();
            Double importe = costo.getCosto();
            String key = generateKey(idA, idB);
            cache.putIfAbsent(key, importe);
            System.out.println("✅ Cache cargada con key: " + key + " => " + importe);
        });
    }

    /*
        Carga los datos en la db local.
        Se comenta porque solo se utiliza la db del contenedor.
     */
//    private void precargarCostosIniciales() {
//        if (costoRepository.count() > 0) return;
//        if (puntoVentaService.getAllPuntosVenta().isEmpty()) throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
//
//        addCostoPuntos(1L, 2L, 2.0);
//        addCostoPuntos(1L, 3L, 3.0);
//        addCostoPuntos(2L, 3L, 5.0);
//        addCostoPuntos(2L, 4L, 10.0);
//        addCostoPuntos(1L, 4L, 11.0);
//        addCostoPuntos(4L, 5L, 5.0);
//        addCostoPuntos(2L, 5L, 14.0);
//        addCostoPuntos(6L, 7L, 32.0);
//        addCostoPuntos(8L, 9L, 11.0);
//        addCostoPuntos(10L, 7L, 5.0);
//        addCostoPuntos(3L, 8L, 10.0);
//        addCostoPuntos(5L, 8L, 30.0);
//        addCostoPuntos(10L, 5L, 5.0);
//        addCostoPuntos(4L, 6L, 6.0);
//    }

    public void addCostoPuntos(Long idA, Long idB, Double costo) {
        if (costo < 0) {
            throw new IllegalArgumentException(COSTO_PUNTOS_LESS_THAN_ZERO);
        }
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, idA) || !puntoVentaExists(puntos, idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }

        String key = generateKey(idA, idB);
        cache.put(key, costo);

        saveCostoToDB(idA, idB, costo);
    }

    public void removeCostoPuntos(Long idA, Long idB) {
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, idA) || !puntoVentaExists(puntos, idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }

        String key = generateKey(idA, idB);
        cache.put(key, 0.0);
    }

    public List<CostoPuntosResponse> getCostosDesdePunto(Long idA) {
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, idA)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        List<CostoPuntosResponse> costos = new ArrayList<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(REGEX);
            Long id1 = Long.valueOf(ids[0]);
            Long id2 = Long.valueOf(ids[1]);

            if (id1.equals(idA)) {
                Long idB = id2;
                if (!puntoVentaExists(puntos, idB)) return;
                String nombrePuntoB = getNombrePuntoVenta(idB, puntos);
                costos.add(new CostoPuntosResponse(idA, idB, value, nombrePuntoB));
            } else if (id2.equals(idA)) {
                Long idB = id1;
                if (!puntoVentaExists(puntos, idB)) return;
                String nombrePuntoB = getNombrePuntoVenta(idB, puntos);
                costos.add(new CostoPuntosResponse(idA, idB, value, nombrePuntoB));
            }
        });
        return costos;
    }

    private String getNombrePuntoVenta(Long id, List<PuntoVenta> puntos) {
        return puntos.stream()
                .filter(p -> p.getId().equals(id))
                .map(PuntoVenta::getNombre)
                .findFirst()
                .orElse(UNKNOWN);
    }

    public List<Long> calcularRutaMinima(Long puntoA, Long puntoB) {
        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> predecesores = new HashMap<>();
        PriorityQueue<Map.Entry<Long, Double>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));

        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, puntoA) || !puntoVentaExists(puntos, puntoB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }

        puntoVentaService.getAllPuntosVenta().forEach(p -> distancias.put(p.getId(), Double.MAX_VALUE));
        distancias.put(puntoA, 0.0);

        pq.add(new AbstractMap.SimpleEntry<>(puntoA, 0.0));

        while (!pq.isEmpty()) {
            long actual = pq.poll().getKey();

            for (Map.Entry<Long, Double> vecino : getVecinos(actual, cache, REGEX).entrySet()) {
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

    public Double calcularCostoTotalRuta(List<Long> ruta) {
        double costoTotal = 0.0;

        for (int i = 0; i < ruta.size() - 1; i++) {
            Long idA = ruta.get(i);
            Long idB = ruta.get(i + 1);
            String key = generateKey(idA, idB);

            Double costo = cache.get(key);
            if (!cache.containsKey(key)) {
                System.err.println("❌ Key faltante en cache: " + key);
                System.err.println("📦 Cache disponible: " + cache);
                throw new IllegalStateException("Falta costo entre " + idA + " y " + idB);
            }

            costoTotal += costo;
        }

        return costoTotal;
    }

    private void saveCostoToDB(Long idA, Long idB, Double costo) {
        Long menor = Math.min(idA, idB);
        Long mayor = Math.max(idA, idB);

        Optional<CostoPuntos> existente = costoRepository.findByIdAAndIdB(menor, mayor);
        if (existente.isPresent()) {
            CostoPuntos costoExistente = existente.get();
            costoExistente.setCosto(costo);
            costoRepository.save(costoExistente);
        } else {
            CostoPuntos nuevo = new CostoPuntos();
            nuevo.setIdA(menor);
            nuevo.setIdB(mayor);
            nuevo.setCosto(costo);
            costoRepository.save(nuevo);
        }
    }

    public void eliminarCostosRelacionadosA(Long id) {
        cache.keySet().removeIf(key -> {
            String[] partes = key.split(REGEX);
            return partes[0].equals(id.toString()) || partes[1].equals(id.toString());
        });

        List<CostoPuntos> costos = costoRepository.findAll();
        for (CostoPuntos costo : costos) {
            if (Objects.equals(costo.getIdA(), id) || Objects.equals(costo.getIdB(), id)) {
                costoRepository.delete(costo);
            }
        }
    }
}
