package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AcreditacionRequest;
import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.service.AcreditacionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acreditaciones")
public class AcreditacionController {

    private final AcreditacionServiceImpl service;

    @Autowired
    public AcreditacionController(AcreditacionServiceImpl service) {
        this.service = service;
    }

    /*
     * Metodo encargado de recibir la acreditacion para determinado idPuntoVenta, con su
     * respectivo importe, y luego almacenarlo en la BBDD.
     */
    @PostMapping
    public ResponseEntity<Acreditacion> recibirAcreditacion(@RequestBody @Valid AcreditacionRequest request) {
        return ResponseEntity.ok(service.recibirAcreditacion(request.getImporte(), request.getIdPuntoVenta()));
    }

    /*
     * Metodo encargado de obtener todas las acreditaciones disponibles en la BBDD.
     */
    @GetMapping
    public ResponseEntity<Iterable<Acreditacion>> obtenerAcreditaciones() {
        return ResponseEntity.ok(service.obtenerAcreditaciones());
    }
}