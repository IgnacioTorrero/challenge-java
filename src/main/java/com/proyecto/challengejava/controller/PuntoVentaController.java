package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PuntoVentaRequest;
import com.proyecto.challengejava.dto.PuntoVentaResponse;
import com.proyecto.challengejava.hateoas.PuntoVentaModelAssembler;
import com.proyecto.challengejava.service.PuntoVentaManager;
import com.proyecto.challengejava.service.PuntoVentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de puntos de venta.
 */
@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaService service;
    private final PuntoVentaManager manager;
    private final PuntoVentaModelAssembler assembler;

    /**
     * Constructor que inyecta los servicios y ensamblador necesarios.
     *
     * @param service   Servicio principal de operaciones sobre puntos de venta.
     * @param manager   Componente encargado de operaciones complejas y eliminación con relaciones.
     * @param assembler Ensamblador HATEOAS para respuestas de puntos de venta.
     */
    @Autowired
    public PuntoVentaController(PuntoVentaService service, PuntoVentaManager manager,
                                PuntoVentaModelAssembler assembler) {
        this.service = service;
        this.manager = manager;
        this.assembler = assembler;
    }

    /**
     * Endpoint para obtener todos los puntos de venta existentes.
     *
     * @return Colección HATEOAS con los puntos de venta registrados.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<PuntoVentaResponse>> getAllPuntosVenta() {
        List<PuntoVentaResponse> responses = service.getAllPuntosVenta().stream()
                .map(p -> new PuntoVentaResponse(p.getId(), p.getNombre()))
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(responses));
    }

    /**
     * Endpoint para registrar un nuevo punto de venta.
     *
     * @param request Objeto con el nombre del nuevo punto de venta.
     * @return Respuesta HTTP 200 OK si fue agregado correctamente.
     */
    @PostMapping
    public ResponseEntity<Void> addPuntoVenta(@RequestBody @Valid PuntoVentaRequest request) {
        service.addPuntoVenta(request.getNombre());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para actualizar el nombre de un punto de venta existente.
     *
     * @param id      ID del punto de venta a modificar.
     * @param request Objeto con el nuevo nombre.
     * @return Respuesta HTTP 200 OK si fue actualizado correctamente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePuntoVenta(@PathVariable Long id, @RequestBody @Valid PuntoVentaRequest request) {
        service.updatePuntoVenta(id, request.getNombre());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para eliminar un punto de venta y sus relaciones asociadas.
     *
     * @param id ID del punto de venta a eliminar.
     * @return Respuesta HTTP 200 OK si fue eliminado correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePuntoVenta(@PathVariable Long id) {
        manager.eliminarPuntoVentaConCostos(id);
        return ResponseEntity.ok().build();
    }
}