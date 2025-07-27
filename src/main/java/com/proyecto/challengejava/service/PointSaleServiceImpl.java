package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.repository.PointSaleRepository;
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
public class PointSaleServiceImpl implements PointSaleService {

    private final Map<Long, String> cache = new ConcurrentHashMap<>();
    private final PointSaleRepository pointSaleRepository;

    @Autowired
    private Environment env;

    /**
     * Constructor that injects the sales point repository.
     *
     * @param pointSaleRepository JPA repository for persisting sales points.
     */
    public PointSaleServiceImpl(PointSaleRepository pointSaleRepository) {
        this.pointSaleRepository = pointSaleRepository;
    }

    /**
     * Initializes the cache unless running in the test environment.
     * Automatically executed after the bean is constructed.
     */
    @PostConstruct
    public void init() {
        if (!Arrays.asList(env.getActiveProfiles()).contains("test")) {
            preloadCache();
        }
    }

    /**
     * Retrieves all sales points registered in the database.
     *
     * @return List of {@link PointSale} entities.
     */
    public List<PointSale> getAllPointSale() {
        return pointSaleRepository.findAll();
    }

    /**
     * Preloads the in-memory cache with all sales points from the database.
     */
    private void preloadCache() {
        List<PointSale> points = pointSaleRepository.findAll();
        for (PointSale p : points) {
            cache.put(p.getId(), p.getName());
        }
    }

    /**
     * Adds a new sales point if no other exists with the same name.
     *
     * @param name Name of the sales point to create.
     * @throws IllegalArgumentException if a point with the same name already exists.
     */
    @Override
    public void addPointSale(String name) {
        if (pointSaleRepository.existsByName(name)) {
            throw new IllegalArgumentException(POINT_OF_SALE_ALREADY_EXISTS);
        }
        PointSale pointSale = new PointSale();
        pointSale.setName(name);
        pointSale = pointSaleRepository.save(pointSale);
        cache.put(pointSale.getId(), name);
    }

    /**
     * Updates the name of an existing sales point.
     *
     * @param id     ID of the sales point to update.
     * @param name New name for the sales point.
     * @throws IllegalArgumentException if the ID does not correspond to an existing point.
     */
    public void updatePointSale(Long id, String name) {
        PointSale pointSale = pointSaleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(POINT_OF_SALE_NOT_FOUND));
        pointSale.setName(name);
        pointSaleRepository.save(pointSale);
        cache.put(id, name);
    }

    /**
     * Deletes a sales point from both the database and the cache.
     *
     * @param id ID of the sales point to delete.
     * @throws IllegalArgumentException if the ID does not exist in the database.
     */
    public void deletePointSale(Long id) {
        if (!pointSaleRepository.existsById(id)) {
            throw new IllegalArgumentException(POINT_OF_SALE_NOT_FOUND);
        }
        pointSaleRepository.deleteById(id);
        cache.remove(id);
    }
}