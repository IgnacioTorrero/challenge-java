package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.service.AcreditacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.proyecto.challengejava.constants.Constantes.*;

@RestController
@RequestMapping("/api/acreditaciones")
public class AcreditacionController {

    private final AcreditacionService service;

    @Autowired
    public AcreditacionController(AcreditacionService service) {
        this.service = service;
    }

    /*
     * Metodo encargado de recibir la acreditacion para determinado idPuntoVenta, con su
     * respectivo importe, y luego almacenarlo en la BBDD.
     */
    @PostMapping
    public ResponseEntity<Acreditacion> recibirAcreditacion(@RequestParam Double importe,
                                                            @RequestParam Long idPuntoVenta) {
        validarParametros(importe, idPuntoVenta);
        return ResponseEntity.ok(service.recibirAcreditacion(importe, idPuntoVenta));
    }

    /*
     * Metodo encargado de obtener todas las acreditaciones disponibles en la BBDD.
     */
    @GetMapping
    public ResponseEntity<Iterable<Acreditacion>> obtenerAcreditaciones() {
        return ResponseEntity.ok(service.obtenerAcreditaciones());
    }

    /*
     * Metodo auxiliar para validar los parámetros de acreditación
     */
    private void validarParametros(Double importe, Long idPuntoVenta) {
        if (importe == null || importe <= 0) {
            throw new IllegalArgumentException(IMPORTE_LESS_THAN_ZERO);
        }
        if (idPuntoVenta == null || idPuntoVenta <= 0) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION_3);
        }
    }
}