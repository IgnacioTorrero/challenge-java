package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.service.CostoPuntosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/costos")
public class CostoPuntosController {

    private final CostoPuntosService service;

    @Autowired
    public CostoPuntosController(CostoPuntosService service) {
        this.service = service;
    }

    /*
     * Metodo encargado de agregar el costo entre punto de venta A y punto de venta B.
     */
    @PostMapping
    public ResponseEntity<Void> addCostoPuntos(@RequestParam Long idA,
                                               @RequestParam Long idB,
                                               @RequestParam Double costo) {
        service.addCostoPuntos(idA, idB, costo);
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de eliminar el costo entre punto de venta A y punto de venta B.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestParam Long idA,
                                                  @RequestParam Long idB) {
        service.removeCostoPuntos(idA, idB);
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de traer una lista de todos los puntos de venta y costos relacionados
     * al punto de venta A.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<List<CostoPuntos>> getCostosDesdePunto(@PathVariable Long idA) {
        return ResponseEntity.ok(service.getCostosDesdePunto(idA));
    }

    @GetMapping("/minimo")
    public ResponseEntity<List<Long>> calcularCostoMinimo(@RequestParam("idA") Long idA, @RequestParam("idB") Long idB) {
        if (idA == null || idB == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<Long> ruta = service.calcularRutaMinima(idA, idB);
        if (ruta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ruta);
    }
}
