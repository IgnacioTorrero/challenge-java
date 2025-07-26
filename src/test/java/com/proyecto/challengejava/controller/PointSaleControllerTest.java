package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PointSaleRequest;
import com.proyecto.challengejava.dto.PointSaleResponse;
import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.hateoas.PointSaleModelAssembler;
import com.proyecto.challengejava.service.PointSaleManager;
import com.proyecto.challengejava.service.PointSaleServiceImpl;
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

/**
 * Unit test for {@link PointSaleController}.
 * Validates the correct behavior of endpoints related to sales points.
 */
public class PointSaleControllerTest {

    @Mock
    private PointSaleServiceImpl service;

    @Mock
    private PointSaleManager pointSaleManager;

    @Mock
    private PointSaleModelAssembler assembler;

    @InjectMocks
    private PointSaleController controller;

    private final PointSale punto1 = new PointSale();
    private final PointSale punto2 = new PointSale();
    private final PointSaleRequest request = new PointSaleRequest();

    /**
     * Initializes mocks and common test data before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        punto1.setId(ID_PUNTO_VENTA);
        punto1.setNombre(PUNTO_VENTA_1);
        punto2.setId(ID_PUNTO_VENTA2);
        punto2.setNombre(PUNTO_VENTA_2);

        request.setNombre(PUNTO_VENTA_3);
    }

    /**
     * Verifies that the {@code getAllPuntosVenta} method correctly returns
     * a list of sales points converted to a CollectionModel.
     */
    @Test
    void getAllPointsSaleResponses() {
        // Arrange
        List<PointSale> puntosVenta = Arrays.asList(punto1, punto2);
        when(service.getAllPuntosVenta()).thenReturn(puntosVenta);
        when(assembler.toModel(any(PointSaleResponse.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<CollectionModel<PointSaleResponse>> response = controller.getAllPointsSale();

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        CollectionModel<PointSaleResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getContent().size());

        List<PointSaleResponse> resultList = body.getContent().stream().toList();

        assertTrue(resultList.stream().anyMatch(p ->
                ID_PUNTO_VENTA.equals(p.getId()) && PUNTO_VENTA_1.equals(p.getNombre())
        ));
        assertTrue(resultList.stream().anyMatch(p ->
                ID_PUNTO_VENTA2.equals(p.getId()) && PUNTO_VENTA_2.equals(p.getNombre())
        ));

        verify(service, times(1)).getAllPuntosVenta();
    }

    /**
     * Verifies that the {@code addPuntoVenta} method returns HTTP 200 OK
     * and invokes the service to create a new sales point.
     */
    @Test
    void addPointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.addPointSale(request);
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        verify(service, times(1)).addPuntoVenta(PUNTO_VENTA_3);
    }

    /**
     * Verifies that the {@code updatePuntoVenta} method correctly updates
     * the name of the given sales point.
     */
    @Test
    void updatePointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.updatePointSale(ID_PUNTO_VENTA5, request);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).updatePuntoVenta(ID_PUNTO_VENTA5, PUNTO_VENTA_3);
    }

    /**
     * Verifies that the {@code deletePuntoVenta} method properly deletes a sales point
     * along with its associated costs.
     */
    @Test
    void deletePointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.deletePointSale(ID_PUNTO_VENTA4);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(pointSaleManager, times(1)).eliminarPuntoVentaConCostos(ID_PUNTO_VENTA4);
    }
}