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

    private final CostPointsRequest request = new CostPointsRequest(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request.setIdA(ID_PUNTO_VENTA5);
        request.setIdB(ID_PUNTO_VENTA2);
    }

    /**
     * Verifies that the {@code addCostoPuntos} method returns HTTP 200 OK
     * and correctly invokes the service.
     */
    @Test
    void addCostPoints_ReturnsOk() {
        ResponseEntity<Void> response = controller.addCostPoints(request, IMPORTE);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).addCostPoints(ID_PUNTO_VENTA5, ID_PUNTO_VENTA2, IMPORTE);
    }

    /**
     * Verifies that the {@code removeCostoPuntos} method returns HTTP 200 OK
     * and invokes the service to remove the cost.
     */
    @Test
    void removeCostPoints_ReturnsOk() {
        ResponseEntity<Void> response = controller.removeCostPoints(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).removeCostPoints(ID_PUNTO_VENTA5, ID_PUNTO_VENTA2);
    }

    /**
     * Verifies that the {@code getCostosDesdePunto} method correctly returns
     * the costs from a sales point in HATEOAS format.
     */
    @Test
    void getCostosDesdePunto_ReturnsCollectionModelOfCostos() {
        // Arrange
        List<CostPointsResponse> costos = Arrays.asList(
                new CostPointsResponse(1L, 2L, 100.0, "GBA_1"),
                new CostPointsResponse(1L, 3L, 150.0, "GBA_2")
        );

        when(service.getCostsFromPoint(ID_PUNTO_VENTA)).thenReturn(costos);
        when(assembler.toModel(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<CollectionModel<CostPointsResponse>> response = controller.getCostsFromPoint(ID_PUNTO_VENTA);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        CollectionModel<CostPointsResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(costos.size(), body.getContent().size());

        for (CostPointsResponse esperado : costos) {
            assertTrue(body.getContent().stream().anyMatch(item ->
                    item != null &&
                            item.getIdA().equals(esperado.getIdA()) &&
                            item.getIdB().equals(esperado.getIdB()) &&
                            item.getCost().equals(esperado.getCost()) &&
                            item.getPointNameB().equals(esperado.getPointNameB())
            ));
        }

        verify(service, times(1)).getCostsFromPoint(ID_PUNTO_VENTA);
    }

    /**
     * Verifies that the {@code calcularCostoMinimo} method returns the expected route and total cost,
     * with the corresponding HATEOAS links.
     */
    @Test
    void calculateMinCostResponse() {
        List<Long> ruta = Arrays.asList(1L, 2L, 3L);
        Double costoTotal = 25.0;
        MinCostRouteResponse original = new MinCostRouteResponse(ruta, costoTotal);

        MinCostRouteResponse responseConLinks = new MinCostRouteResponse(ruta, costoTotal);
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(1L)).withRel("ver-costos-desde-1"));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(2L)).withRel("ver-costos-desde-2"));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).getCostsFromPoint(3L)).withRel("ver-costos-desde-3"));
        responseConLinks.add(linkTo(methodOn(CostPointsController.class).calculateMinCost(new CostPointsRequest(1L, 3L))).withRel("recalcular-ruta"));

        when(service.calculateMinPath(anyLong(), anyLong())).thenReturn(ruta);
        when(service.calculateTotalRouteCost(anyList())).thenReturn(costoTotal);
        when(rutaAssembler.toModel(any())).thenReturn(responseConLinks);

        // Act
        CostPointsRequest request = new CostPointsRequest(1L, 3L);
        ResponseEntity<MinCostRouteResponse> response = controller.calculateMinCost(request);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ruta, response.getBody().getRute());
        assertEquals(costoTotal, response.getBody().getTotalCost());

        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-1"));
        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-2"));
        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-3"));
        assertTrue(response.getBody().getLinks().hasLink("recalcular-ruta"));

        verify(service, times(1)).calculateMinPath(anyLong(), anyLong());
        verify(service, times(1)).calculateTotalRouteCost(anyList());
        verify(rutaAssembler, times(1)).toModel(any());
    }

    /**
     * Verifies that if a request with equal IDs is sent, the {@code addCostoPuntos}
     * method throws an {@link IllegalArgumentException}.
     */
    @Test
    void addCostPoints_SameId_ThrowsException() {
        // Arrange
        Long id = 5L;
        CostPointsRequest requestConIdsIguales = new CostPointsRequest(id, id);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.addCostPoints(requestConIdsIguales, 100.0);
        });

        assertEquals(INVALID_ID_EXCEPTION, exception.getMessage());
    }
}