package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostoPuntosRequest;
import com.proyecto.challengejava.dto.CostoPuntosResponse;
import com.proyecto.challengejava.dto.RutaCostoMinimoResponse;
import com.proyecto.challengejava.hateoas.CostoPuntosModelAssembler;
import com.proyecto.challengejava.hateoas.RutaCostoMinimoModelAssembler;
import com.proyecto.challengejava.service.CostoPuntosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.proyecto.challengejava.constants.Constantes.*;

@RestController
@RequestMapping("/api/costos")
public class CostoPuntosController {

    private final CostoPuntosService service;
    private final CostoPuntosModelAssembler costoPuntosModelAssembler;
    private final RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler;

    @Autowired
    public CostoPuntosController(CostoPuntosService service, CostoPuntosModelAssembler costoPuntosModelAssembler,
                                 RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler) {
        this.service = service;
        this.costoPuntosModelAssembler = costoPuntosModelAssembler;
        this.rutaCostoMinimoModelAssembler = rutaCostoMinimoModelAssembler;
    }

    /*
     * Metodo encargado de agregar el costo entre punto de venta A y punto de venta B.
     */
    @PostMapping
    public ResponseEntity<Void> addCostoPuntos(@RequestBody @Valid CostoPuntosRequest request,
                                               @RequestParam Double costo) {
        validarParametros(request.getIdA(), request.getIdB());
        service.addCostoPuntos(request.getIdA(), request.getIdB(), costo);
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de eliminar el costo entre punto de venta A y punto de venta B.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestBody @Valid CostoPuntosRequest request) {
        validarParametros(request.getIdA(), request.getIdB());
        service.removeCostoPuntos(request.getIdA(), request.getIdB());
        return ResponseEntity.ok().build();
    }

    /*
     * Metodo encargado de traer una lista de todos los puntos de venta y costos relacionados
     * al punto de venta A.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<CollectionModel<CostoPuntosResponse>> getCostosDesdePunto(@PathVariable Long idA) {
        List<CostoPuntosResponse> responseList = service.getCostosDesdePunto(idA)
                .stream()
                .map(costoPuntosModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(responseList));
    }

    /*
    * Metodo encargado de calcular la ruta y el costo minimo entre dos puntos de venta.
    *
    * Este endpoint técnicamente es un GET, porque no modifica datos.
    * Pero se define como POST para que Swagger UI pueda enviarlo correctamente,
    * ya que no permite body en métodos GET (según restricciones del protocolo HTTP)
     */
    @PostMapping("/minimo")
    public ResponseEntity<RutaCostoMinimoResponse> calcularCostoMinimo(@RequestBody @Valid CostoPuntosRequest request) {
        validarParametros(request.getIdA(), request.getIdB());

        List<Long> ruta = service.calcularRutaMinima(request.getIdA(), request.getIdB());
        Double costoTotal = service.calcularCostoTotalRuta(ruta);

        RutaCostoMinimoResponse response = new RutaCostoMinimoResponse(ruta, costoTotal);
        return ResponseEntity.ok(rutaCostoMinimoModelAssembler.toModel(response));
    }

    /*
     * Metodo auxiliar para validar si ambos IDs son identicos
     */
    private void validarParametros(Long idA, Long idB) {
        if (idA.equals(idB)) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
    }
}
