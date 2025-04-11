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

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaService service;
    private final PuntoVentaManager manager;
    private final PuntoVentaModelAssembler assembler;

    @Autowired
    public PuntoVentaController(PuntoVentaService service, PuntoVentaManager manager,
                                PuntoVentaModelAssembler assembler) {
        this.service = service;
        this.manager = manager;
        this.assembler = assembler;
    }

    /*
     * Metodo encargado de traer una lista de todos los puntos de venta existentes.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<PuntoVentaResponse>> getAllPuntosVenta() {
        List<PuntoVentaResponse> responses = service.getAllPuntosVenta().stream()
                .map(p -> new PuntoVentaResponse(p.getId(), p.getNombre()))
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(responses));
    }

    /*
     * Metodo encargado de agregar un punto de venta con su respectivo id y nombre.
     */
    @PostMapping
    public ResponseEntity<Void> addPuntoVenta(@RequestBody @Valid PuntoVentaRequest request) {
        service.addPuntoVenta(request.getNombre());
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de actualizar el nombre del id de punto de venta ingresado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePuntoVenta(@PathVariable Long id, @RequestBody @Valid PuntoVentaRequest request) {
        service.updatePuntoVenta(id, request.getNombre());
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de eliminar el punto de venta relacionado al id ingresado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePuntoVenta(@PathVariable Long id) {
        manager.eliminarPuntoVentaConCostos(id);
        return ResponseEntity.ok().build();
    }
}
