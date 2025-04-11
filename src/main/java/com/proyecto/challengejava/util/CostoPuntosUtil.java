package com.proyecto.challengejava.util;

import com.proyecto.challengejava.entity.PuntoVenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.proyecto.challengejava.constants.Constantes.REGEX;

public class CostoPuntosUtil {

    private CostoPuntosUtil() {}

    public static String generateKey(Long idA, Long idB) {
        return (idA < idB ? idA + REGEX + idB : idB + REGEX + idA);
    }

    public static Map<Long, Double> getVecinos(Long punto, ConcurrentHashMap<String, Double> cache, String separator) {
        Map<Long, Double> vecinos = new HashMap<>();
        cache.forEach((key, value) -> {
            String[] ids = key.split(separator);
            Long id1 = Long.valueOf(ids[0]);
            Long id2 = Long.valueOf(ids[1]);

            if (id1.equals(punto)) {
                vecinos.put(id2, value);
            } else if (id2.equals(punto)) {
                vecinos.put(id1, value);
            }
        });
        return vecinos;
    }

    public static boolean puntoVentaExists(List<PuntoVenta> puntos, Long id) {
        return puntos.stream().anyMatch(p -> p.getId().equals(id));
    }
}
