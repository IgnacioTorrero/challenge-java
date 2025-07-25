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

/**
 * Controlador REST para gestionar los costos entre puntos de venta y calcular rutas óptimas.
 */
@RestController
@RequestMapping("/api/costos")
public class CostoPuntosController {

    private final CostoPuntosService service;
    private final CostoPuntosModelAssembler costoPuntosModelAssembler;
    private final RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler;

    /**
     * Constructor que inyecta los servicios y ensambladores necesarios.
     *
     * @param service                     Servicio de lógica de negocio para costos entre puntos.
     * @param costoPuntosModelAssembler  Ensamblador HATEOAS para respuestas de costos.
     * @param rutaCostoMinimoModelAssembler Ensamblador HATEOAS para la ruta de costo mínimo.
     */
    @Autowired
    public CostoPuntosController(CostoPuntosService service, CostoPuntosModelAssembler costoPuntosModelAssembler,
                                 RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler) {
        this.service = service;
        this.costoPuntosModelAssembler = costoPuntosModelAssembler;
        this.rutaCostoMinimoModelAssembler = rutaCostoMinimoModelAssembler;
    }

    /**
     * Endpoint para agregar un costo entre dos puntos de venta.
     *
     * @param request Objeto con los IDs de los puntos A y B.
     * @param costo   Valor del costo entre los puntos.
     * @return Respuesta HTTP 200 OK si se agregó correctamente.
     */
    @PostMapping
    public ResponseEntity<Void> addCostoPuntos(@RequestBody @Valid CostoPuntosRequest request,
                                               @RequestParam Double costo) {
        validarParametros(request.getIdA(), request.getIdB());
        service.addCostoPuntos(request.getIdA(), request.getIdB(), costo);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para eliminar el costo entre dos puntos de venta.
     *
     * @param request Objeto con los IDs de los puntos A y B.
     * @return Respuesta HTTP 200 OK si se eliminó correctamente.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestBody @Valid CostoPuntosRequest request) {
        validarParametros(request.getIdA(), request.getIdB());
        service.removeCostoPuntos(request.getIdA(), request.getIdB());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para obtener todos los costos desde un punto de venta específico.
     *
     * @param idA ID del punto de venta origen.
     * @return Colección HATEOAS de costos desde el punto especificado.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<CollectionModel<CostoPuntosResponse>> getCostosDesdePunto(@PathVariable Long idA) {
        List<CostoPuntosResponse> responseList = service.getCostosDesdePunto(idA)
                .stream()
                .map(costoPuntosModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(responseList));
    }

    /**
     * Endpoint para calcular la ruta de costo mínimo entre dos puntos de venta.
     *
     * <p>Se usa POST en lugar de GET por limitaciones de Swagger UI para enviar body en GET.</p>
     *
     * @param request Objeto con los IDs de los puntos A y B.
     * @return Modelo HATEOAS con la ruta y el costo total.
     */
    @PostMapping("/minimo")
    public ResponseEntity<RutaCostoMinimoResponse> calcularCostoMinimo(@RequestBody @Valid CostoPuntosRequest request) {
        validarParametros(request.getIdA(), request.getIdB());

        List<Long> ruta = service.calcularRutaMinima(request.getIdA(), request.getIdB());
        Double costoTotal = service.calcularCostoTotalRuta(ruta);

        RutaCostoMinimoResponse response = new RutaCostoMinimoResponse(ruta, costoTotal);
        return ResponseEntity.ok(rutaCostoMinimoModelAssembler.toModel(response));
    }

    /**
     * Metodo auxiliar para validar que los IDs de los puntos de venta no sean iguales.
     *
     * @param idA ID del punto A.
     * @param idB ID del punto B.
     * @throws IllegalArgumentException si ambos IDs son iguales.
     */
    private void validarParametros(Long idA, Long idB) {
        if (idA.equals(idB)) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
    }
}