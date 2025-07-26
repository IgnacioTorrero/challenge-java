package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.entity.CostPoints;
import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.exception.PointSaleNotFoundException;
import com.proyecto.challengejava.repository.CostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constants.*;
import static com.proyecto.challengejava.util.CostPointsUtil.*;

/**
 * Implementation of the service that manages connection costs between sales points,
 * using an in-memory cache and database persistence.
 */
@Service
public class CostPointsServiceImpl implements CostPointsService {

    private final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
    private final PointSaleService pointSaleService;
    private final CostRepository costRepository;

    /**
     * Constructor that injects the required services.
     *
     * @param pointSaleService Service for managing sales points.
     * @param costRepository   Repository for persisting costs.
     */
    public CostPointsServiceImpl(PointSaleService pointSaleService, CostRepository costRepository) {
        this.pointSaleService = pointSaleService;
        this.costRepository = costRepository;
    }

    /**
     * Initializes the cache by loading data from the database.
     * This method is automatically executed after the bean is constructed.
     */
    @PostConstruct
    public void init() {
        // Wait for sales points to be preloaded
        List<PointSale> puntos = pointSaleService.getAllPointSale();
        // Initial cost preloading could be done here if needed
        loadCacheFromDB();
    }

    /**
     * Loads all costs from the database and stores them in the cache.
     */
    public void loadCacheFromDB() {
        costRepository.findAll().forEach(cost -> {
            Long idA = cost.getIdA();
            Long idB = cost.getIdB();
            Double amount = cost.getCost();
            String key = generateKey(idA, idB);
            cache.putIfAbsent(key, amount);
            System.out.println("✅ Cache loaded with key: " + key + " => " + amount);
        });
    }

    /**
     * Adds or updates the cost between two sales points.
     *
     * @param idA   ID of the first point.
     * @param idB   ID of the second point.
     * @param cost Cost value between the two points.
     * @throws IllegalArgumentException if the cost is negative or any point doesn't exist.
     */
    public void addCostPoints(Long idA, Long idB, Double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException(COSTO_PUNTOS_LESS_THAN_ZERO);
        }
        List<PointSale> puntos = pointSaleService.getAllPointSale();
        if (!puntoVentaExists(puntos, idA) || !puntoVentaExists(puntos, idB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }

        String key = generateKey(idA, idB);
        cache.put(key, cost);

        saveCostToDB(idA, idB, cost);
    }

    /**
     * Removes the cost between two sales points by replacing its value with 0 in the cache.
     *
     * @param idA ID of the first point.
     * @param idB ID of the second point.
     * @throws PointSaleNotFoundException if any of the points do not exist.
     */
    public void removeCostPoints(Long idA, Long idB) {
        List<PointSale> points = pointSaleService.getAllPointSale();
        if (!puntoVentaExists(points, idA) || !puntoVentaExists(points, idB)) {
            throw new PointSaleNotFoundException(PUNTO_VENTA_NOT_FOUND);
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
    public List<CostPointsResponse> getCostsFromPoint(Long idA) {
        List<PointSale> points = pointSaleService.getAllPointSale();
        if (!puntoVentaExists(points, idA)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        List<CostPointsResponse> costs = new ArrayList<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(REGEX);
            Long id1 = Long.valueOf(ids[0]);
            Long id2 = Long.valueOf(ids[1]);

            if (id1.equals(idA)) {
                Long idB = id2;
                if (!puntoVentaExists(points, idB)) return;
                String pointBName = getPointSaleName(idB, points);
                costs.add(new CostPointsResponse(idA, idB, value, pointBName));
            } else if (id2.equals(idA)) {
                Long idB = id1;
                if (!puntoVentaExists(points, idB)) return;
                String nombrePuntoB = getPointSaleName(idB, points);
                costs.add(new CostPointsResponse(idA, idB, value, nombrePuntoB));
            }
        });
        return costs;
    }

    /**
     * Retrieves the name of a sales point by its ID.
     *
     * @param id     ID of the point.
     * @param points List of sales points.
     * @return Name of the point or a default value if not found.
     */
    private String getPointSaleName(Long id, List<PointSale> points) {
        return points.stream()
                .filter(p -> p.getId().equals(id))
                .map(PointSale::getName)
                .findFirst()
                .orElse(UNKNOWN);
    }

    /**
     * Calculates the lowest cost route between two sales points using Dijkstra's algorithm.
     *
     * @param pointA ID of the origin point.
     * @param pointB ID of the destination point.
     * @return List of point IDs representing the optimal route.
     * @throws IllegalArgumentException if any of the points do not exist.
     */
    public List<Long> calculateMinPath(Long pointA, Long pointB) {
        Map<Long, Double> distances = new HashMap<>();
        Map<Long, Long> predecessors = new HashMap<>();
        PriorityQueue<Map.Entry<Long, Double>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));

        List<PointSale> points = pointSaleService.getAllPointSale();
        if (!puntoVentaExists(points, pointA) || !puntoVentaExists(points, pointB)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }

        pointSaleService.getAllPointSale().forEach(p -> distances.put(p.getId(), Double.MAX_VALUE));
        distances.put(pointA, 0.0);

        pq.add(new AbstractMap.SimpleEntry<>(pointA, 0.0));

        while (!pq.isEmpty()) {
            long actual = pq.poll().getKey();

            for (Map.Entry<Long, Double> neighbor : getVecinos(actual, cache, REGEX).entrySet()) {
                double newCost = distances.get(actual) + neighbor.getValue();
                if (newCost < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), newCost);
                    predecessors.put(neighbor.getKey(), actual);
                    pq.add(new AbstractMap.SimpleEntry<>(neighbor.getKey(), newCost));
                }
            }
        }

        List<Long> rute = new ArrayList<>();
        Long current = pointB;
        while (current != null) {
            rute.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(rute);
        return rute;
    }

    /**
     * Calculates the total cost of a route composed of sales point IDs.
     *
     * @param rute List of IDs representing the route.
     * @return Total sum of costs between each pair of consecutive points.
     * @throws IllegalStateException if any cost is missing in the cache.
     */
    public Double calculateTotalRouteCost(List<Long> rute) {
        double totalCost = 0.0;

        for (int i = 0; i < rute.size() - 1; i++) {
            Long idA = rute.get(i);
            Long idB = rute.get(i + 1);
            String key = generateKey(idA, idB);

            Double cost = cache.get(key);
            if (!cache.containsKey(key)) {
                System.err.println("❌ Key faltante en cache: " + key);
                System.err.println("📦 Cache disponible: " + cache);
                throw new IllegalStateException("Missing cost between " + idA + " and " + idB);
            }

            totalCost += cost;
        }

        return totalCost;
    }

    /**
     * Saves or updates the cost between two sales points in the database.
     *
     * @param idA   ID of the first point.
     * @param idB   ID of the second point.
     * @param cost Cost between them.
     */
    private void saveCostToDB(Long idA, Long idB, Double cost) {
        Long minor = Math.min(idA, idB);
        Long mayor = Math.max(idA, idB);

        Optional<CostPoints> existing = costRepository.findByIdAAndIdB(minor, mayor);
        if (existing.isPresent()) {
            CostPoints existingCost = existing.get();
            existingCost.setCost(cost);
            costRepository.save(existingCost);
        } else {
            CostPoints newCost = new CostPoints();
            newCost.setIdA(minor);
            newCost.setIdB(mayor);
            newCost.setCost(cost);
            costRepository.save(newCost);
        }
    }

    /**
     * Removes all cached and persisted costs related to the specified point.
     *
     * @param id ID of the sales point to delete.
     */
    public void deleteRelatedCostsTo(Long id) {
        cache.keySet().removeIf(key -> {
            String[] parts = key.split(REGEX);
            return parts[0].equals(id.toString()) || parts[1].equals(id.toString());
        });

        List<CostPoints> costs = costRepository.findAll();
        for (CostPoints cost : costs) {
            if (Objects.equals(cost.getIdA(), id) || Objects.equals(cost.getIdB(), id)) {
                costRepository.delete(cost);
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