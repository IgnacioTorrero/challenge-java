package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PuntoVentaRequest;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.service.PuntoVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.proyecto.challengejava.constants.Constantes.*;

@RestController
@RequestMapping("/api/puntos-venta")
public class PuntoVentaController {

    private final PuntoVentaService service;

    @Autowired
    public PuntoVentaController(PuntoVentaService service) {
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
    public ResponseEntity<Void> addPuntoVenta(@RequestBody PuntoVentaRequest request) {
        validarPuntoVenta(request.getId(), request.getNombre());
        service.addPuntoVenta(request.getId(), request.getNombre());
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de actualizar el nombre del id de punto de venta ingresado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePuntoVenta(@PathVariable Long id,
                                                 @RequestParam String nombre) {
        validarPuntoVenta(id, nombre);
        service.updatePuntoVenta(id, nombre);
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

    private void validarPuntoVenta(Long id, String nombre) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
        if (nombre == null || nombre.trim().isEmpty() || nombre.equals("\"\"") || (!nombre.matches(REGEX_2))) {
            throw new IllegalArgumentException(INVALID_NAME_EXCEPTION);
        }
    }
}
