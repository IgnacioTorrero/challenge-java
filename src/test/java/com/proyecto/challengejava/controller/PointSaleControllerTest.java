package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.PuntoVentaRequest;
import com.proyecto.challengejava.dto.PuntoVentaResponse;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.hateoas.PuntoVentaModelAssembler;
import com.proyecto.challengejava.service.PuntoVentaManager;
import com.proyecto.challengejava.service.PuntoVentaServiceImpl;
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
    private PuntoVentaServiceImpl service;

    @Mock
    private PuntoVentaManager puntoVentaManager;

    @Mock
    private PuntoVentaModelAssembler assembler;

    @InjectMocks
    private PointSaleController controller;

    private final PuntoVenta punto1 = new PuntoVenta();
    private final PuntoVenta punto2 = new PuntoVenta();
    private final PuntoVentaRequest request = new PuntoVentaRequest();

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
    void getAllPuntosVenta_ReturnsCollectionModelOfPuntoVentaResponses() {
        // Arrange
        List<PuntoVenta> puntosVenta = Arrays.asList(punto1, punto2);
        when(service.getAllPuntosVenta()).thenReturn(puntosVenta);
        when(assembler.toModel(any(PuntoVentaResponse.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<CollectionModel<PuntoVentaResponse>> response = controller.getAllPuntosVenta();

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        CollectionModel<PuntoVentaResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.getContent().size());

        List<PuntoVentaResponse> resultList = body.getContent().stream().toList();

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
    void addPuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.addPuntoVenta(request);
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        verify(service, times(1)).addPuntoVenta(PUNTO_VENTA_3);
    }

    /**
     * Verifies that the {@code updatePuntoVenta} method correctly updates
     * the name of the given sales point.
     */
    @Test
    void updatePuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.updatePuntoVenta(ID_PUNTO_VENTA5, request);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).updatePuntoVenta(ID_PUNTO_VENTA5, PUNTO_VENTA_3);
    }

    /**
     * Verifies that the {@code deletePuntoVenta} method properly deletes a sales point
     * along with its associated costs.
     */
    @Test
    void deletePuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.deletePuntoVenta(ID_PUNTO_VENTA4);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(puntoVentaManager, times(1)).eliminarPuntoVentaConCostos(ID_PUNTO_VENTA4);
    }
}