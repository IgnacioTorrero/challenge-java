package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.dto.MinCostRouteResponse;
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

import static com.proyecto.challengejava.constants.Constants.*;

/**
 * REST controller for managing connection costs between sales points and calculating optimal routes.
 */
@RestController
@RequestMapping("/api/costos")
public class CostPointsController {

    private final CostoPuntosService service;
    private final CostoPuntosModelAssembler costoPuntosModelAssembler;
    private final RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler;

    /**
     * Constructor that injects required services and assemblers.
     *
     * @param service                     Service handling business logic for point-to-point costs.
     * @param costoPuntosModelAssembler  HATEOAS assembler for cost responses.
     * @param rutaCostoMinimoModelAssembler HATEOAS assembler for minimum cost route responses.
     */
    @Autowired
    public CostPointsController(CostoPuntosService service, CostoPuntosModelAssembler costoPuntosModelAssembler,
                                RutaCostoMinimoModelAssembler rutaCostoMinimoModelAssembler) {
        this.service = service;
        this.costoPuntosModelAssembler = costoPuntosModelAssembler;
        this.rutaCostoMinimoModelAssembler = rutaCostoMinimoModelAssembler;
    }

    /**
     * Endpoint to add a connection cost between two sales points.
     *
     * @param request Object containing the IDs of points A and B.
     * @param costo   Cost value between the points.
     * @return HTTP 200 OK response if added successfully.
     */
    @PostMapping
    public ResponseEntity<Void> addCostoPuntos(@RequestBody @Valid CostPointsRequest request,
                                               @RequestParam Double costo) {
        validarParametros(request.getIdA(), request.getIdB());
        service.addCostoPuntos(request.getIdA(), request.getIdB(), costo);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to remove the connection cost between two sales points.
     *
     * @param request Object containing the IDs of points A and B.
     * @return HTTP 200 OK response if removed successfully.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostoPuntos(@RequestBody @Valid CostPointsRequest request) {
        validarParametros(request.getIdA(), request.getIdB());
        service.removeCostoPuntos(request.getIdA(), request.getIdB());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to retrieve all connection costs from a specific sales point.
     *
     * @param idA ID of the origin sales point.
     * @return HATEOAS collection of costs from the specified point.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<CollectionModel<CostPointsResponse>> getCostosDesdePunto(@PathVariable Long idA) {
        List<CostPointsResponse> responseList = service.getCostosDesdePunto(idA)
                .stream()
                .map(costoPuntosModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(responseList));
    }

    /**
     * Endpoint to calculate the minimum cost route between two sales points.
     *
     * <p>POST is used instead of GET due to Swagger UI limitations with GET requests that contain a body.</p>
     *
     * @param request Object containing the IDs of points A and B.
     * @return HATEOAS model with the route and total cost.
     */
    @PostMapping("/minimo")
    public ResponseEntity<MinCostRouteResponse> calcularCostoMinimo(@RequestBody @Valid CostPointsRequest request) {
        validarParametros(request.getIdA(), request.getIdB());

        List<Long> ruta = service.calcularRutaMinima(request.getIdA(), request.getIdB());
        Double costoTotal = service.calcularCostoTotalRuta(ruta);

        MinCostRouteResponse response = new MinCostRouteResponse(ruta, costoTotal);
        return ResponseEntity.ok(rutaCostoMinimoModelAssembler.toModel(response));
    }

    /**
     * Helper method to validate that the sales point IDs are not equal.
     *
     * @param idA ID of point A.
     * @param idB ID of point B.
     * @throws IllegalArgumentException if both IDs are the same.
     */
    private void validarParametros(Long idA, Long idB) {
        if (idA.equals(idB)) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
    }
}