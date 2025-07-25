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
 * REST controller for managing sales points.
 */
@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaService service;
    private final PuntoVentaManager manager;
    private final PuntoVentaModelAssembler assembler;

    /**
     * Constructor that injects the required services and assembler.
     *
     * @param service   Main service for operations on sales points.
     * @param manager   Component responsible for complex operations and deletion with relationships.
     * @param assembler HATEOAS assembler for sales point responses.
     */
    @Autowired
    public PuntoVentaController(PuntoVentaService service, PuntoVentaManager manager,
                                PuntoVentaModelAssembler assembler) {
        this.service = service;
        this.manager = manager;
        this.assembler = assembler;
    }

    /**
     * Endpoint to retrieve all existing sales points.
     *
     * @return HATEOAS collection with the registered sales points.
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
     * Endpoint to register a new sales point.
     *
     * @param request Object containing the name of the new sales point.
     * @return HTTP 200 OK response if added successfully.
     */
    @PostMapping
    public ResponseEntity<Void> addPuntoVenta(@RequestBody @Valid PuntoVentaRequest request) {
        service.addPuntoVenta(request.getNombre());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to update the name of an existing sales point.
     *
     * @param id      ID of the sales point to be updated.
     * @param request Object containing the new name.
     * @return HTTP 200 OK response if updated successfully.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePuntoVenta(@PathVariable Long id, @RequestBody @Valid PuntoVentaRequest request) {
        service.updatePuntoVenta(id, request.getNombre());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to delete a sales point and its associated relationships.
     *
     * @param id ID of the sales point to be deleted.
     * @return HTTP 200 OK response if deleted successfully.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePuntoVenta(@PathVariable Long id) {
        manager.eliminarPuntoVentaConCostos(id);
        return ResponseEntity.ok().build();
    }
}