package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.PuntoVentaRepository;
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
 * Implementación del servicio de puntos de venta.
 * Gestiona operaciones CRUD y una cache en memoria.
 */
@Service
public class PuntoVentaServiceImpl implements PuntoVentaService {

    private final Map<Long, String> cache = new ConcurrentHashMap<>();
    private final PuntoVentaRepository puntoVentaRepository;

    @Autowired
    private Environment env;

    /**
     * Constructor que inyecta el repositorio de puntos de venta.
     *
     * @param puntoVentaRepository Repositorio JPA para persistencia de puntos de venta.
     */
    public PuntoVentaServiceImpl(PuntoVentaRepository puntoVentaRepository) {
        this.puntoVentaRepository = puntoVentaRepository;
    }

    /**
     * Inicializa la cache si no se está ejecutando en el entorno de test.
     * Se ejecuta automáticamente después de construir el bean.
     */
    @PostConstruct
    public void init() {
//        precargarPuntosVentaSiNoExisten();
        if (!Arrays.asList(env.getActiveProfiles()).contains("test")) {
            precargarCache();
        }
    }

    /*
     * Método alternativo para cargar cache desde la base de datos.
     * Actualmente no utilizado.
     */
//    private void cargarCacheDesdeDB() {
//        puntoVentaRepository.findAll().forEach(p -> cache.put(p.getId(), p.getNombre()));
//    }

    /*
     * Precarga puntos de venta en la base si no existen.
     * Comentado porque solo se usa la base del contenedor.
     */
//    private void precargarPuntosVentaSiNoExisten() {
//        if (puntoVentaRepository.count() == 0) {
//            List<PuntoVenta> puntos = new ArrayList<>();
//            for (long i = 1; i <= 10; i++) {
//                PuntoVenta puntoVenta = new PuntoVenta();
//                puntoVenta.setNombre(PUNTOS_VENTA.get((int) (i - 1)));
//                puntos.add(puntoVenta);
//            }
//            puntoVentaRepository.saveAll(puntos);
//        }
//    }

    /**
     * Obtiene todos los puntos de venta registrados en la base de datos.
     *
     * @return Lista de entidades {@link PuntoVenta}.
     */
    public List<PuntoVenta> getAllPuntosVenta() {
        return puntoVentaRepository.findAll();
    }

    /**
     * Precarga la cache en memoria con todos los puntos de venta existentes en la base.
     */
    private void precargarCache() {
        List<PuntoVenta> puntos = puntoVentaRepository.findAll();
        for (PuntoVenta p : puntos) {
            cache.put(p.getId(), p.getNombre());
        }
    }

    /**
     * Agrega un nuevo punto de venta si no existe otro con el mismo nombre.
     *
     * @param nombre Nombre del punto de venta a crear.
     * @throws IllegalArgumentException si ya existe un punto con el mismo nombre.
     */
    @Override
    public void addPuntoVenta(String nombre) {
        if (puntoVentaRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException(PUNTO_VENTA_ALREADY_EXISTS);
        }
        PuntoVenta puntoVenta = new PuntoVenta();
        puntoVenta.setNombre(nombre);
        puntoVenta = puntoVentaRepository.save(puntoVenta);
        cache.put(puntoVenta.getId(), nombre);
    }

    /**
     * Actualiza el nombre de un punto de venta existente.
     *
     * @param id     ID del punto de venta a modificar.
     * @param nombre Nuevo nombre para el punto de venta.
     * @throws IllegalArgumentException si el ID no corresponde a un punto existente.
     */
    public void updatePuntoVenta(Long id, String nombre) {
        PuntoVenta puntoVenta = puntoVentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND));
        puntoVenta.setNombre(nombre);
        puntoVentaRepository.save(puntoVenta);
        cache.put(id, nombre);
    }

    /**
     * Elimina un punto de venta tanto de la base de datos como del cache.
     *
     * @param id ID del punto de venta a eliminar.
     * @throws IllegalArgumentException si el ID no existe en la base.
     */
    public void deletePuntoVenta(Long id) {
        if (!puntoVentaRepository.existsById(id)) {
            throw new IllegalArgumentException(PUNTO_VENTA_NOT_FOUND);
        }
        puntoVentaRepository.deleteById(id);
        cache.remove(id);
    }
}