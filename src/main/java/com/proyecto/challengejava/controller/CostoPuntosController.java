package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.service.CostoPuntosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costos")
public class CostoPuntosController {

    private final CostoPuntosService service;

    @Autowired
    public CostoPuntosController(CostoPuntosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> addCostoPuntos(@RequestParam Long idA, @RequestParam Long idB, @RequestParam Double costo) {
        service.addCostoPuntos(idA, idB, costo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestParam Long idA, @RequestParam Long idB) {
        service.removeCostoPuntos(idA, idB);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idA}")
    public ResponseEntity<List<CostoPuntos>> getCostosDesdePunto(@PathVariable Long idA) {
        return ResponseEntity.ok(service.getCostosDesdePunto(idA));
    }
}
