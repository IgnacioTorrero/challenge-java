package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PuntoVentaRequest;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.service.PuntoVentaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaServiceImpl service;

    @Autowired
    public PuntoVentaController(PuntoVentaServiceImpl service) {
        this.service = service;
    }

    /*
     * Metodo encargado de traer una lista de todos los puntos de venta existentes.
     */
    @GetMapping
    public ResponseEntity<List<PuntoVenta>> getAllPuntosVenta() {
        return ResponseEntity.ok(service.getAllPuntosVenta());
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
        service.deletePuntoVenta(id);
        return ResponseEntity.ok().build();
    }
}
