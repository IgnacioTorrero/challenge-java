package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostPointsResponse;
import com.proyecto.challengejava.entity.CostPoints;
import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.exception.PointSaleNotFoundException;
import com.proyecto.challengejava.repository.CostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.proyecto.challengejava.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link CostPointsServiceImpl}.
 * Validates business logic related to cost management between sales points.
 */
public class CostPointsServiceImplTest {

    @Mock
    private PointSaleService pointSaleService;

    @Mock
    private PointSaleServiceImpl puntoVentaServiceImpl;

    private CostPointsServiceImpl costoPuntosServiceImpl;

    @Mock
    private CostRepository costRepository;

    /**
     * Initializes mocks and preloads mock sales points and cost data into cache.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock initial sales points and cost repository data
        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(1L); setName(POINTS_OF_SALE.get(0)); }},
                new PointSale() {{ setId(2L); setName(POINTS_OF_SALE.get(1)); }},
                new PointSale() {{ setId(3L); setName(POINTS_OF_SALE.get(2)); }},
                new PointSale() {{ setId(4L); setName(POINTS_OF_SALE.get(3)); }},
                new PointSale() {{ setId(5L); setName(POINTS_OF_SALE.get(4)); }},
                new PointSale() {{ setId(6L); setName(POINTS_OF_SALE.get(5)); }},
                new PointSale() {{ setId(7L); setName(POINTS_OF_SALE.get(6)); }},
                new PointSale() {{ setId(8L); setName(POINTS_OF_SALE.get(7)); }},
                new PointSale() {{ setId(9L); setName(POINTS_OF_SALE.get(8)); }},
                new PointSale() {{ setId(10L); setName(POINTS_OF_SALE.get(9)); }}
        ));

        when(costRepository.findAll()).thenReturn(Arrays.asList(
                new CostPoints() {{ setIdA(1L); setIdB(2L); setCost(2.0); }},
                new CostPoints() {{ setIdA(1L); setIdB(3L); setCost(3.0); }},
                new CostPoints() {{ setIdA(1L); setIdB(4L); setCost(4.0); }}
        ));

        costoPuntosServiceImpl = new CostPointsServiceImpl(puntoVentaServiceImpl, costRepository);

        // Preload cache
        costoPuntosServiceImpl.loadCacheFromDB();
    }

    /**
     * Verifies that {@code addCostPoints} stores a new cost and updates the cache.
     */
    @Test
    void addCostPoints_ReturnsOk() {
        costoPuntosServiceImpl.addCostPoints(ID_POINT_SALE1, ID_POINT_SALE2, AMOUNT);

        List<CostPointsResponse> costs = costoPuntosServiceImpl.getCostsFromPoint(ID_POINT_SALE1);

        assertEquals(3, costs.size());
        assertEquals(AMOUNT, costs.get(0).getCost());
    }

    /**
     * Verifies that {@code addCostPoints} throws exception if the cost is less than zero.
     */
    @Test
    void addCostPointsLessThanZero_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostPoints(ID_POINT_SALE1, ID_POINT_SALE2, INVALID_COST)
        );
        assertEquals(COST_POINTS_LESS_THAN_ZERO, exception.getMessage());
    }

    /**
     * Verifies that {@code addCostPoints} throws exception when a sales point does not exist.
     */
    @Test
    void addCostPointsNotFound_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostPoints(INVALID_ID, INVALID_ID2, AMOUNT)
        );
        assertEquals(POINT_OF_SALE_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code removeCostPoints} removes a cost and sets it to zero in cache.
     */
    @Test
    void removeCostPoints_ReturnsOk() {
        costoPuntosServiceImpl.removeCostPoints(ID_POINT_SALE1, ID_POINT_SALE2);
        double actualCost = costoPuntosServiceImpl.getCostsFromPoint(ID_POINT_SALE1).get(0).getCost();
        assertEquals(0.0, actualCost);
    }

    /**
     * Verifies that {@code getCostsFromPoint} returns all related costs.
     */
    @Test
    void getCostsFromPoint_ReturnsCostPoints() {
        costoPuntosServiceImpl.addCostPoints(ID_POINT_SALE1, ID_POINT_SALE2, AMOUNT);
        costoPuntosServiceImpl.addCostPoints(ID_POINT_SALE1, ID_POINT_SALE3, AMOUNT2);

        List<CostPointsResponse> costs = costoPuntosServiceImpl.getCostsFromPoint(ID_POINT_SALE1);

        assertEquals(3, costs.size());
        assertEquals(AMOUNT, costs.get(0).getCost());
        assertEquals(AMOUNT2, costs.get(1).getCost());
    }

    /**
     * Verifies that {@code getCostsFromPoint} throws exception for invalid sales point ID.
     */
    @Test
    void getCostsFromPoint_ThrowsExceptionIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.getCostsFromPoint(INVALID_ID)
        );
        assertEquals(POINT_OF_SALE_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code removeCostPoints} throws exception when sales points are invalid.
     */
    @Test
    void removeCostPoints_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(PointSaleNotFoundException.class, () ->
                costoPuntosServiceImpl.removeCostPoints(INVALID_ID, INVALID_ID2)
        );
        assertEquals(POINT_OF_SALE_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code loadCacheFromDB} successfully loads initial data.
     */
    @Test
    void loadCacheFromDB_ReturnsOk() {
        costoPuntosServiceImpl.loadCacheFromDB();

        List<CostPointsResponse> costsFromPoint1 = costoPuntosServiceImpl.getCostsFromPoint(ID_POINT_SALE1);

        assertFalse(costsFromPoint1.isEmpty());
        assertEquals(3, costsFromPoint1.size());
    }

    /**
     * Verifies that {@code calculateMinPath} throws exception if any sales point doesn't exist.
     */
    @Test
    void calculateMinPath_ThrowsIllegalArgumentException_WhenPointSaleDoesNotExist() {
        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(1L); setName(POINTS_OF_SALE.get(0)); }},
                new PointSale() {{ setId(2L); setName(POINTS_OF_SALE.get(1)); }}
        ));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            costoPuntosServiceImpl.calculateMinPath(1L, 3L);
        });

        assertEquals("Punto/s de venta no encontrado/s", exception.getMessage());
    }

    /**
     * Verifies that {@code calculateTotalRouteCost} returns correct total cost of a path.
     */
    @Test
    void calculate() {
        List<Long> ruta = Arrays.asList(1L, 2L); // Cost 2.0 between 1 and 2

        double totalCost = costoPuntosServiceImpl.calculateTotalRouteCost(ruta);

        assertEquals(2.0, totalCost);
    }

    /**
     * Verifies that {@code calculateTotalRouteCost} throws exception when cost is missing in cache.
     */
    @Test
    void calculateTotalRouteCost_ThrowsException_WhenKeyNotInCache() {
        List<Long> invalidRute = Arrays.asList(2L, 3L);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            costoPuntosServiceImpl.calculateTotalRouteCost(invalidRute);
        });

        assertEquals("Falta costo entre 2 y 3", exception.getMessage());
    }

    /**
     * Verifies that {@code deleteRelatedCostsToA} removes all costs linked to a sales point.
     */
    @Test
    void deleteRelatedCostsTo_DeletesFromRepository() {
        Long id = 2L;

        CostPoints cost1 = new CostPoints(); cost1.setIdA(1L); cost1.setIdB(2L); cost1.setCost(2.0);
        CostPoints cost2 = new CostPoints(); cost2.setIdA(2L); cost2.setIdB(3L); cost2.setCost(2.5);
        CostPoints cost3 = new CostPoints(); cost3.setIdA(4L); cost3.setIdB(5L); cost3.setCost(3.0); // unrelated

        when(costRepository.findAll()).thenReturn(Arrays.asList(cost1, cost2, cost3));

        costoPuntosServiceImpl.deleteRelatedCostsTo(id);

        verify(costRepository).delete(cost1);
        verify(costRepository).delete(cost2);
        verify(costRepository, never()).delete(cost3);
    }

    /**
     * Verifies that {@code calculateMinPath} returns the shortest path between two points.
     */
    @Test
    void calculateMinPath_ReturnsCorrectPath() {
        // Arrange
        Long pointA = 1L;
        Long pointB = 4L;

        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(1L); setName("P1"); }},
                new PointSale() {{ setId(2L); setName("P2"); }},
                new PointSale() {{ setId(3L); setName("P3"); }},
                new PointSale() {{ setId(4L); setName("P4"); }}
        ));

        costoPuntosServiceImpl.getCache().clear();
        costoPuntosServiceImpl.addCostPoints(1L, 2L, 1.0);
        costoPuntosServiceImpl.addCostPoints(2L, 3L, 1.0);
        costoPuntosServiceImpl.addCostPoints(3L, 4L, 1.0);

        // Act
        List<Long> ruta = costoPuntosServiceImpl.calculateMinPath(pointA, pointB);

        // Assert
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L), ruta);
    }

    /**
     * Verifies that {@code getCostsFromPoint} works even if the point is the destination (idB).
     */
    @Test
    void getCostsFromPoint_ReturnsCostWhenIsId2() {
        // Arrange
        Long idA = 2L;
        Long relatedId = 1L;

        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(idA); setName("Punto 2"); }},
                new PointSale() {{ setId(relatedId); setName("Punto 1"); }}
        ));

        costoPuntosServiceImpl.getCache().clear();
        costoPuntosServiceImpl.addCostPoints(relatedId, idA, 5.5); // creates key "1-2"

        // Act
        List<CostPointsResponse> costs = costoPuntosServiceImpl.getCostsFromPoint(idA);

        // Assert
        assertEquals(1, costs.size());
        assertEquals(idA, costs.get(0).getIdA());
        assertEquals(relatedId, costs.get(0).getIdB());
        assertEquals(5.5, costs.get(0).getCost());
        assertEquals("Punto 1", costs.get(0).getPointNameB());
    }

    /**
     * Verifies that {@code saveCostToDB} updates the cost if it already exists in the database.
     */
    @Test
    void saveCostToDB_updateExistingCost() {
        // Arrange
        Long idA = 1L;
        Long idB = 2L;
        Double newCost = 15.0;
        Long minor = Math.min(idA, idB);
        Long mayor = Math.max(idA, idB);

        CostPoints existing = new CostPoints();
        existing.setIdA(minor);
        existing.setIdB(mayor);
        existing.setCost(10.0);

        when(costRepository.findByIdAAndIdB(minor, mayor)).thenReturn(Optional.of(existing));

        // Act
        when(pointSaleService.getAllPointSale()).thenReturn(List.of(
                new PointSale(idA, "A"),
                new PointSale(idB, "B")
        ));

        costoPuntosServiceImpl.addCostPoints(idA, idB, newCost);

        // Assert
        assertEquals(newCost, existing.getCost());
        verify(costRepository).save(existing);
    }
}