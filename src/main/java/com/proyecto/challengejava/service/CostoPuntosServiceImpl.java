package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.CostoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constants.*;
import static com.proyecto.challengejava.util.CostoPuntosUtil.*;

/**
 * Implementation of the service that manages connection costs between sales points,
 * using an in-memory cache and database persistence.
 */
@Service
public class CostoPuntosServiceImpl implements CostoPuntosService {

    private final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
    private final PuntoVentaService puntoVentaService;
    private final CostoRepository costoRepository;

    /**
     * Constructor that injects the required services.
     *
     * @param puntoVentaService Service for managing sales points.
     * @param costoRepository   Repository for persisting costs.
     */
    public CostoPuntosServiceImpl(PuntoVentaService puntoVentaService, CostoRepository costoRepository) {
        this.puntoVentaService = puntoVentaService;
        this.costoRepository = costoRepository;
    }

    /**
     * Initializes the cache by loading data from the database.
     * This method is automatically executed after the bean is constructed.
     */
    @PostConstruct
    public void init() {
        // Wait for sales points to be preloaded
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        // Initial cost preloading could be done here if needed
        cargarCacheDesdeDB();
    }

    /**
     * Loads all costs from the database and stores them in the cache.
     */
    public void cargarCacheDesdeDB() {
        costoRepository.findAll().forEach(costo -> {
            Long idA = costo.getIdA();
            Long idB = costo.getIdB();
            Double importe = costo.getCosto();
            String key = generateKey(idA, idB);
            cache.putIfAbsent(key, importe);
            System.out.println("✅ Cache loaded with key: " + key + " => " + importe);
        });
    }

    /**
     * Adds or updates the cost between two sales points.
     *
     * @param idA   ID of the first point.
     * @param idB   ID of the second point.
     * @param costo Cost value between the two points.
     * @throws IllegalArgumentException if the cost is negative or any point doesn't exist.
     */
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

    /**
     * Removes the cost between two sales points by replacing its value with 0 in the cache.
     *
     * @param idA ID of the first point.
     * @param idB ID of the second point.
     * @throws PuntoVentaNotFoundException if any of the points do not exist.
     */
    public void removeCostoPuntos(Long idA, Long idB) {
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, idA) || !puntoVentaExists(puntos, idB)) {
            throw new PuntoVentaNotFoundException(PUNTO_VENTA_NOT_FOUND);
        }

        String key = generateKey(idA, idB);
        cache.put(key, 0.0);
    }

    /**
     * Retrieves all connection costs from a specific sales point.
     *
     * @param idA ID of the origin sales point.
     * @return List of responses with cost information from that point.
     * @throws IllegalArgumentException if the point does not exist.
     */
    public List<CostPointsResponse> getCostosDesdePunto(Long idA) {
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        if (!puntoVentaExists(puntos, idA)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        List<CostPointsResponse> costos = new ArrayList<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(REGEX);
            Long id1 = Long.valueOf(ids[0]);
            Long id2 = Long.valueOf(ids[1]);

            if (id1.equals(idA)) {
                Long idB = id2;
                if (!puntoVentaExists(puntos, idB)) return;
                String nombrePuntoB = getNombrePuntoVenta(idB, puntos);
                costos.add(new CostPointsResponse(idA, idB, value, nombrePuntoB));
            } else if (id2.equals(idA)) {
                Long idB = id1;
                if (!puntoVentaExists(puntos, idB)) return;
                String nombrePuntoB = getNombrePuntoVenta(idB, puntos);
                costos.add(new CostPointsResponse(idA, idB, value, nombrePuntoB));
            }
        });
        return costos;
    }

    /**
     * Retrieves the name of a sales point by its ID.
     *
     * @param id     ID of the point.
     * @param puntos List of sales points.
     * @return Name of the point or a default value if not found.
     */
    private String getNombrePuntoVenta(Long id, List<PuntoVenta> puntos) {
        return puntos.stream()
                .filter(p -> p.getId().equals(id))
                .map(PuntoVenta::getNombre)
                .findFirst()
                .orElse(UNKNOWN);
    }

    /**
     * Calculates the lowest cost route between two sales points using Dijkstra's algorithm.
     *
     * @param puntoA ID of the origin point.
     * @param puntoB ID of the destination point.
     * @return List of point IDs representing the optimal route.
     * @throws IllegalArgumentException if any of the points do not exist.
     */
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

    /**
     * Calculates the total cost of a route composed of sales point IDs.
     *
     * @param ruta List of IDs representing the route.
     * @return Total sum of costs between each pair of consecutive points.
     * @throws IllegalStateException if any cost is missing in the cache.
     */
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

    /**
     * Saves or updates the cost between two sales points in the database.
     *
     * @param idA   ID of the first point.
     * @param idB   ID of the second point.
     * @param costo Cost between them.
     */
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

    /**
     * Removes all cached and persisted costs related to the specified point.
     *
     * @param id ID of the sales point to delete.
     */
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

    /**
     * Returns the current content of the cost cache.
     *
     * @return Map with keys formatted as ID_A-ID_B and values as cost amounts.
     */
    public Map<String, Double> getCache() {
        return cache;
    }
}