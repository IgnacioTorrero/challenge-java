package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PointSaleRequest;
import com.proyecto.challengejava.dto.PointSaleResponse;
import com.proyecto.challengejava.hateoas.PointSaleModelAssembler;
import com.proyecto.challengejava.service.PointSaleManager;
import com.proyecto.challengejava.service.PointSaleService;
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
public class PointSaleController {

    private final PointSaleService service;
    private final PointSaleManager manager;
    private final PointSaleModelAssembler assembler;

    /**
     * Constructor that injects the required services and assembler.
     *
     * @param service   Main service for operations on sales points.
     * @param manager   Component responsible for complex operations and deletion with relationships.
     * @param assembler HATEOAS assembler for sales point responses.
     */
    @Autowired
    public PointSaleController(PointSaleService service, PointSaleManager manager,
                               PointSaleModelAssembler assembler) {
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
    public ResponseEntity<CollectionModel<PointSaleResponse>> getAllPuntosVenta() {
        List<PointSaleResponse> responses = service.getAllPuntosVenta().stream()
                .map(p -> new PointSaleResponse(p.getId(), p.getNombre()))
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
    public ResponseEntity<Void> addPuntoVenta(@RequestBody @Valid PointSaleRequest request) {
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
    public ResponseEntity<Void> updatePuntoVenta(@PathVariable Long id, @RequestBody @Valid PointSaleRequest request) {
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