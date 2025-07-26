package com.proyecto.challengejava.util;

import com.proyecto.challengejava.entity.PointSale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constants.*;

public class CostPointsUtil {

    private CostPointsUtil() {}

    public static String generateKey(Long idA, Long idB) {
        return (idA < idB ? idA + REGEX + idB : idB + REGEX + idA);
    }

    public static Map<Long, Double> getNeighbors(Long point, ConcurrentHashMap<String, Double> cache, String separator) {
        Map<Long, Double> neighbors = new HashMap<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(separator);
            Long id1 = Long.valueOf(ids[0]);
            Long id2 = Long.valueOf(ids[1]);

            if (id1.equals(point)) {
                neighbors.put(id2, value);
            } else if (id2.equals(point)) {
                neighbors.put(id1, value);
            }
        });
        return neighbors;
    }

    public static boolean pointSaleExists(List<PointSale> points, Long id) {
        return points.stream().anyMatch(p -> p.getId().equals(id));
    }
}
