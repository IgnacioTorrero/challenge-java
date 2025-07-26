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

    private final PointSale point1 = new PointSale();
    private final PointSale point2 = new PointSale();
    private final PointSaleRequest request = new PointSaleRequest();

    /**
     * Initializes mocks and common test data before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        point1.setId(ID_POINT_SALE1);
        point1.setName(POINT_SALE_1);
        point2.setId(ID_POINT_SALE2);
        point2.setName(POINT_SALE_2);

        request.setName(POINT_SALE_3);
    }

    /**
     * Verifies that the {@code getAllPointsSale} method correctly returns
     * a list of sales points converted to a CollectionModel.
     */
    @Test
    void getAllPointsSaleResponses() {
        // Arrange
        List<PointSale> salePoints = Arrays.asList(point1, point2);
        when(service.getAllPointSale()).thenReturn(salePoints);
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
                ID_POINT_SALE1.equals(p.getId()) && POINT_SALE_1.equals(p.getName())
        ));
        assertTrue(resultList.stream().anyMatch(p ->
                ID_POINT_SALE2.equals(p.getId()) && POINT_SALE_2.equals(p.getName())
        ));

        verify(service, times(1)).getAllPointSale();
    }

    /**
     * Verifies that the {@code addPointSale} method returns HTTP 200 OK
     * and invokes the service to create a new sales point.
     */
    @Test
    void addPointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.addPointSale(request);
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        verify(service, times(1)).addPointSale(POINT_SALE_3);
    }

    /**
     * Verifies that the {@code updatePointSale} method correctly updates
     * the name of the given sales point.
     */
    @Test
    void updatePointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.updatePointSale(ID_POINT_SALE5, request);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).updatePointSale(ID_POINT_SALE5, POINT_SALE_3);
    }

    /**
     * Verifies that the {@code deletePointSale} method properly deletes a sales point
     * along with its associated costs.
     */
    @Test
    void deletePointSale_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.deletePointSale(ID_POINT_SALE4);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(pointSaleManager, times(1)).deletePointSaleWithCosts(ID_POINT_SALE4);
    }
}