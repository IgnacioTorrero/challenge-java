package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostPointsRequest;
import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.dto.MinCostRouteResponse;
import com.proyecto.challengejava.hateoas.CostPointsModelAssembler;
import com.proyecto.challengejava.hateoas.MinCostRouteModelAssembler;
import com.proyecto.challengejava.service.CostPointsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Unit test for {@link CostPointsController}.
 * Verifies the behavior of endpoints related to connection costs between sales points.
 */
public class CostPointsControllerTest {

    @Mock
    private CostPointsServiceImpl service;

    @Mock
    private CostPointsModelAssembler assembler;

    @Mock
    private MinCostRouteModelAssembler rutaAssembler;

    @InjectMocks
    private CostPointsController controller;

    private final CostPointsRequest request = new CostPointsRequest(ID_POINT_SALE1, ID_POINT_SALE2);

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request.setIdA(ID_POINT_SALE5);
        request.setIdB(ID_POINT_SALE2);
    }

    /**
     * Verifies that the {@code addCostPoints} method returns HTTP 200 OK
     * and correctly invokes the service.
     */
    @Test
    void addCostPoints_ReturnsOk() {
        ResponseEntity<Void> response = controller.addCostPoints(request, AMOUNT);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).addCostPoints(ID_POINT_SALE5, ID_POINT_SALE2, AMOUNT);
    }

    /**
     * Verifies that the {@code removeCostPoints} method returns HTTP 200 OK
     * and invokes the service to remove the cost.
     */
    @Test
    void removeCostPoints_ReturnsOk() {
        ResponseEntity<Void> response = controller.removeCostPoints(request);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).removeCostPoints(ID_POINT_SALE5, ID_POINT_SALE2);
    }

    /**
     * Verifies that the {@code getCostsFromPoint} method correctly returns
     * the costs from a sales point in HATEOAS format.
     */
    @Test
    void getCostsFromPoint_ReturnsCollectionModelOfCosts() {
        // Arrange
        List<CostPointsResponse> costs = Arrays.asList(
                new CostPointsResponse(ID_POINT_SALE1, ID_POINT_SALE2, AMOUNT, POINT_SALE_2),
                new CostPointsResponse(ID_POINT_SALE1, ID_POINT_SALE3, AMOUNT3, POINT_SALE_4)
        );

        when(service.getCostsFromPoint(ID_POINT_SALE1)).thenReturn(costs);
        when(assembler.toModel(any())).thenAnswer(invocation -> invocation.getArgument(ZERO));

        // Act
        ResponseEntity<CollectionModel<CostPointsResponse>> response = controller.getCostsFromPoint(ID_POINT_SALE1);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        CollectionModel<CostPointsResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(costs.size(), body.getContent().size());

        for (CostPointsResponse expected : costs) {
            assertTrue(body.getContent().stream().anyMatch(item ->
                    item != null &&
                            item.getIdA().equals(expected.getIdA()) &&
                            item.getIdB().equals(expected.getIdB()) &&
                            item.getCost().equals(expected.getCost()) &&
                            item.getPointNameB().equals(expected.getPointNameB())
            ));
        }

        verify(service, times(1)).getCostsFromPoint(ID_POINT_SALE1);
    }

    /**
     * Verifies that the {@code calculateMinCost} method returns the expected route and total cost,
     * with the corresponding HATEOAS links.
     */
    @Test
    void calculateMinCostResponse() {
        List<Long> route = Arrays.asList(ID_POINT_SALE1, ID_POINT_SALE2, ID_POINT_SALE3);
        Double totalCost = 25.0;
        MinCostRouteResponse original = new MinCostRouteResponse(route, totalCost);

        MinCostRouteResponse responseConLinks = new MinCostRouteResponse(route, totalCost);
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(ID_POINT_SALE1)).withRel(SEE_COSTS_FROM_1));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(ID_POINT_SALE2)).withRel(SEE_COSTS_FROM_2));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(ID_POINT_SALE3)).withRel(SEE_COSTS_FROM_3));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).calculateMinCost(new CostPointsRequest(1L, 3L))).withRel(RECALCULATE_ROUTE));

        when(service.calculateMinPath(anyLong(), anyLong())).thenReturn(route);
        when(service.calculateTotalRouteCost(anyList())).thenReturn(totalCost);
        when(rutaAssembler.toModel(any())).thenReturn(responseConLinks);

        // Act
        CostPointsRequest request = new CostPointsRequest(ID_POINT_SALE1, ID_POINT_SALE3);
        ResponseEntity<MinCostRouteResponse> response = controller.calculateMinCost(request);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(route, response.getBody().getRoute());
        assertEquals(totalCost, response.getBody().getTotalCost());

        assertTrue(response.getBody().getLinks().hasLink(SEE_COSTS_FROM_1));
        assertTrue(response.getBody().getLinks().hasLink(SEE_COSTS_FROM_2));
        assertTrue(response.getBody().getLinks().hasLink(SEE_COSTS_FROM_3));
        assertTrue(response.getBody().getLinks().hasLink(RECALCULATE_ROUTE));

        verify(service, times(1)).calculateMinPath(anyLong(), anyLong());
        verify(service, times(1)).calculateTotalRouteCost(anyList());
        verify(rutaAssembler, times(1)).toModel(any());
    }

    /**
     * Verifies that if a request with equal IDs is sent, the {@code addCostPoints}
     * method throws an {@link IllegalArgumentException}.
     */
    @Test
    void addCostPoints_SameId_ThrowsException() {
        // Arrange
        Long id = 5L;
        CostPointsRequest requestWithSameId = new CostPointsRequest(id, id);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.addCostPoints(requestWithSameId, AMOUNT);
        });

        assertEquals(INVALID_ID_EXCEPTION, exception.getMessage());
    }
}