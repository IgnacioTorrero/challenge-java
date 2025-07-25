package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostoPuntosResponse;
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
 * Implementación del servicio que gestiona los costos entre puntos de venta,
 * utilizando un cache en memoria y persistencia en base de datos.
 */
@Service
public class CostoPuntosServiceImpl implements CostoPuntosService {

    private final ConcurrentHashMap<String, Double> cache = new ConcurrentHashMap<>();
    private final PuntoVentaService puntoVentaService;
    private final CostoRepository costoRepository;

    /**
     * Constructor que inyecta los servicios necesarios.
     *
     * @param puntoVentaService Servicio de puntos de venta.
     * @param costoRepository   Repositorio para persistencia de costos.
     */
    public CostoPuntosServiceImpl(PuntoVentaService puntoVentaService, CostoRepository costoRepository) {
        this.puntoVentaService = puntoVentaService;
        this.costoRepository = costoRepository;
    }

    /**
     * Inicializa la cache cargando los datos desde la base de datos.
     * Este metodo se ejecuta automáticamente después de la construcción del bean.
     */
    @PostConstruct
    public void init() {
        // Esperar a que se hayan precargado los puntos de venta
        List<PuntoVenta> puntos = puntoVentaService.getAllPuntosVenta();
        // Se podría precargar costos iniciales aquí, si fuera necesario.
        cargarCacheDesdeDB();
    }

    /**
     * Carga todos los costos desde la base de datos y los almacena en la cache.
     */
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

    /**
     * Agrega o actualiza el costo entre dos puntos de venta.
     *
     * @param idA   ID del primer punto.
     * @param idB   ID del segundo punto.
     * @param costo Valor del costo entre ambos puntos.
     * @throws IllegalArgumentException si el costo es negativo o algún punto no existe.
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
     * Elimina el costo entre dos puntos de venta, reemplazando su valor por 0 en cache.
     *
     * @param idA ID del primer punto.
     * @param idB ID del segundo punto.
     * @throws PuntoVentaNotFoundException si alguno de los puntos no existe.
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
     * Obtiene todos los costos asociados a un punto de venta.
     *
     * @param idA ID del punto de venta de origen.
     * @return Lista de respuestas con información de costos desde ese punto.
     * @throws IllegalArgumentException si el punto no existe.
     */
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

    /**
     * Obtiene el nombre de un punto de venta a partir de su ID.
     *
     * @param id     ID del punto.
     * @param puntos Lista de puntos de venta.
     * @return Nombre del punto o un valor por defecto si no se encuentra.
     */
    private String getNombrePuntoVenta(Long id, List<PuntoVenta> puntos) {
        return puntos.stream()
                .filter(p -> p.getId().equals(id))
                .map(PuntoVenta::getNombre)
                .findFirst()
                .orElse(UNKNOWN);
    }

    /**
     * Calcula la ruta de menor costo entre dos puntos de venta utilizando Dijkstra.
     *
     * @param puntoA ID del punto de origen.
     * @param puntoB ID del punto de destino.
     * @return Lista de IDs de puntos que representan la ruta óptima.
     * @throws IllegalArgumentException si alguno de los puntos no existe.
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
     * Calcula el costo total de una ruta compuesta por IDs de puntos de venta.
     *
     * @param ruta Lista de IDs de la ruta.
     * @return Suma total de los costos entre cada par de puntos consecutivos.
     * @throws IllegalStateException si falta algún costo en la cache.
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
     * Guarda o actualiza el costo entre dos puntos de venta en la base de datos.
     *
     * @param idA   ID del primer punto.
     * @param idB   ID del segundo punto.
     * @param costo Costo entre ambos.
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
     * Elimina todos los costos en cache y base de datos que estén relacionados al punto indicado.
     *
     * @param id ID del punto de venta a eliminar.
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
     * Devuelve el contenido actual de la cache de costos.
     *
     * @return Mapa con claves formadas por ID_A-ID_B y valores de costos.
     */
    public Map<String, Double> getCache() {
        return cache;
    }
}