package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.RutaCostoMinimoResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.service.CostoPuntosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.proyecto.challengejava.constants.Constantes.*;

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
        validarParametros(idA, idB);
        service.addCostoPuntos(idA, idB, costo);
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de eliminar el costo entre punto de venta A y punto de venta B.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestParam Long idA,
                                                  @RequestParam Long idB) {
        validarParametros(idA, idB);
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
    public ResponseEntity<RutaCostoMinimoResponse> calcularCostoMinimo(@RequestParam("idA") Long idA,
                                                                       @RequestParam("idB") Long idB) {
        validarParametros(idA, idB);
        List<Long> ruta = service.calcularRutaMinima(idA, idB);

        Double costoTotal = service.calcularCostoTotalRuta(ruta);
        RutaCostoMinimoResponse response = new RutaCostoMinimoResponse(ruta, costoTotal);
        return ResponseEntity.ok(response);
    }

    /*
     * Metodo auxiliar para validar IDs
     */
    private void validarParametros(Long idA, Long idB) {
        if (idA == null || idB == null || idA <= 0 || idB <= 0) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
        if (idA.equals(idB)) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION_2);
        }
    }
}
