package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.service.AcreditacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acreditaciones")
public class AcreditacionController {

    private final AcreditacionService service;

    @Autowired
    public AcreditacionController(AcreditacionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Acreditacion> recibirAcreditacion(@RequestParam Double importe,
                                                            @RequestParam Long idPuntoVenta) {
        return ResponseEntity.ok(service.recibirAcreditacion(importe, idPuntoVenta));
    }

    @GetMapping
    public ResponseEntity<Iterable<Acreditacion>> obtenerAcreditaciones() {
        return ResponseEntity.ok(service.obtenerAcreditaciones());
    }
}