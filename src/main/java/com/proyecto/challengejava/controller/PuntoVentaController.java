package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.service.PuntoVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaService service;

    @Autowired
    public PuntoVentaController(PuntoVentaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PuntoVenta>> getAllPuntos() {
        return ResponseEntity.ok(service.getAllPuntosVenta());
    }

    @PostMapping
    public ResponseEntity<Void> addPunto(@RequestParam Long id, @RequestParam String nombre) {
        service.addPuntoVenta(id, nombre);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePunto(@PathVariable Long id, @RequestParam String nombre) {
        service.updatePuntoVenta(id, nombre);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePunto(@PathVariable Long id) {
        service.deletePuntoVenta(id);
        return ResponseEntity.ok().build();
    }
}
