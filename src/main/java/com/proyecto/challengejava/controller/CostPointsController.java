package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.dto.MinCostRouteResponse;
import com.proyecto.challengejava.hateoas.CostPointsModelAssembler;
import com.proyecto.challengejava.hateoas.MinCostRouteModelAssembler;
import com.proyecto.challengejava.service.CostPointsService;
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
@RequestMapping("/api/costs")
public class CostPointsController {

    private final CostPointsService service;
    private final CostPointsModelAssembler costPointsModelAssembler;
    private final MinCostRouteModelAssembler minCostRouteModelAssembler;

    /**
     * Constructor that injects required services and assemblers.
     *
     * @param service                     Service handling business logic for point-to-point costs.
     * @param costPointsModelAssembler  HATEOAS assembler for cost responses.
     * @param minCostRouteModelAssembler HATEOAS assembler for minimum cost route responses.
     */
    @Autowired
    public CostPointsController(CostPointsService service, CostPointsModelAssembler costPointsModelAssembler,
                                MinCostRouteModelAssembler minCostRouteModelAssembler) {
        this.service = service;
        this.costPointsModelAssembler = costPointsModelAssembler;
        this.minCostRouteModelAssembler = minCostRouteModelAssembler;
    }

    /**
     * Endpoint to add a connection cost between two sales points.
     *
     * @param request Object containing the IDs of points A and B.
     * @param cost   Cost value between the points.
     * @return HTTP 200 OK response if added successfully.
     */
    @PostMapping
    public ResponseEntity<Void> addCostPoints(@RequestBody @Valid CostPointsRequest request,
                                              @RequestParam Double cost) {
        validateParameters(request.getIdA(), request.getIdB());
        service.addCostPoints(request.getIdA(), request.getIdB(), cost);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to remove the connection cost between two sales points.
     *
     * @param request Object containing the IDs of points A and B.
     * @return HTTP 200 OK response if removed successfully.
     */
    @DeleteMapping
    public ResponseEntity<Void> removeCostPoints(@RequestBody @Valid CostPointsRequest request) {
        validateParameters(request.getIdA(), request.getIdB());
        service.removeCostPoints(request.getIdA(), request.getIdB());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to retrieve all connection costs from a specific sales point.
     *
     * @param idA ID of the origin sales point.
     * @return HATEOAS collection of costs from the specified point.
     */
    @GetMapping("/{idA}")
    public ResponseEntity<CollectionModel<CostPointsResponse>> getCostsFromPoint(@PathVariable Long idA) {
        List<CostPointsResponse> responseList = service.getCostsFromPoint(idA)
                .stream()
                .map(costPointsModelAssembler::toModel)
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
    @PostMapping("/min")
    public ResponseEntity<MinCostRouteResponse> calculateMinCost(@RequestBody @Valid CostPointsRequest request) {
        validateParameters(request.getIdA(), request.getIdB());

        List<Long> route = service.calculateMinPath(request.getIdA(), request.getIdB());
        Double totalCost = service.calculateTotalRouteCost(route);

        MinCostRouteResponse response = new MinCostRouteResponse(route, totalCost);
        return ResponseEntity.ok(minCostRouteModelAssembler.toModel(response));
    }

    /**
     * Helper method to validate that the sales point IDs are not equal.
     *
     * @param idA ID of point A.
     * @param idB ID of point B.
     * @throws IllegalArgumentException if both IDs are the same.
     */
    private void validateParameters(Long idA, Long idB) {
        if (idA.equals(idB)) {
            throw new IllegalArgumentException(INVALID_ID_EXCEPTION);
        }
    }
}