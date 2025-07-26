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
                new PointSale() {{ setId(1L); setName(PUNTOS_VENTA.get(0)); }},
                new PointSale() {{ setId(2L); setName(PUNTOS_VENTA.get(1)); }},
                new PointSale() {{ setId(3L); setName(PUNTOS_VENTA.get(2)); }},
                new PointSale() {{ setId(4L); setName(PUNTOS_VENTA.get(3)); }},
                new PointSale() {{ setId(5L); setName(PUNTOS_VENTA.get(4)); }},
                new PointSale() {{ setId(6L); setName(PUNTOS_VENTA.get(5)); }},
                new PointSale() {{ setId(7L); setName(PUNTOS_VENTA.get(6)); }},
                new PointSale() {{ setId(8L); setName(PUNTOS_VENTA.get(7)); }},
                new PointSale() {{ setId(9L); setName(PUNTOS_VENTA.get(8)); }},
                new PointSale() {{ setId(10L); setName(PUNTOS_VENTA.get(9)); }}
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
     * Verifies that {@code addCostoPuntos} stores a new cost and updates the cache.
     */
    @Test
    void addCostPoints_ReturnsOk() {
        costoPuntosServiceImpl.addCostPoints(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);

        List<CostPointsResponse> costos = costoPuntosServiceImpl.getCostsFromPoint(ID_PUNTO_VENTA);

        assertEquals(3, costos.size());
        assertEquals(IMPORTE, costos.get(0).getCost());
    }

    /**
     * Verifies that {@code addCostoPuntos} throws exception if the cost is less than zero.
     */
    @Test
    void addCostPointsLessThanZero_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostPoints(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, INVALID_COSTO)
        );
        assertEquals(COSTO_PUNTOS_LESS_THAN_ZERO, exception.getMessage());
    }

    /**
     * Verifies that {@code addCostoPuntos} throws exception when a sales point does not exist.
     */
    @Test
    void addCostPointsNotFound_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostPoints(INVALID_ID, INVALID_ID2, IMPORTE)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code removeCostoPuntos} removes a cost and sets it to zero in cache.
     */
    @Test
    void removeCostPoints_ReturnsOk() {
        costoPuntosServiceImpl.removeCostPoints(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);
        double costoAhora = costoPuntosServiceImpl.getCostsFromPoint(ID_PUNTO_VENTA).get(0).getCost();
        assertEquals(0.0, costoAhora);
    }

    /**
     * Verifies that {@code getCostosDesdePunto} returns all related costs.
     */
    @Test
    void getCostsFromPoint_ReturnsCostoPuntos() {
        costoPuntosServiceImpl.addCostPoints(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);
        costoPuntosServiceImpl.addCostPoints(ID_PUNTO_VENTA, ID_PUNTO_VENTA3, IMPORTE2);

        List<CostPointsResponse> costos = costoPuntosServiceImpl.getCostsFromPoint(ID_PUNTO_VENTA);

        assertEquals(3, costos.size());
        assertEquals(IMPORTE, costos.get(0).getCost());
        assertEquals(IMPORTE2, costos.get(1).getCost());
    }

    /**
     * Verifies that {@code getCostosDesdePunto} throws exception for invalid sales point ID.
     */
    @Test
    void getCostsFromPoint_ThrowsExceptionIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.getCostsFromPoint(INVALID_ID)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code removeCostoPuntos} throws exception when sales points are invalid.
     */
    @Test
    void removeCostPoints_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(PointSaleNotFoundException.class, () ->
                costoPuntosServiceImpl.removeCostPoints(INVALID_ID, INVALID_ID2)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    /**
     * Verifies that {@code cargarCacheDesdeDB} successfully loads initial data.
     */
    @Test
    void loadCacheFromDB_ReturnsOk() {
        costoPuntosServiceImpl.loadCacheFromDB();

        List<CostPointsResponse> costosDesdePunto1 = costoPuntosServiceImpl.getCostsFromPoint(ID_PUNTO_VENTA);

        assertFalse(costosDesdePunto1.isEmpty());
        assertEquals(3, costosDesdePunto1.size());
    }

    /**
     * Verifies that {@code calcularRutaMinima} throws exception if any sales point doesn't exist.
     */
    @Test
    void calculateMinPath_ThrowsIllegalArgumentException_WhenPuntoVentaDoesNotExist() {
        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(1L); setName(PUNTOS_VENTA.get(0)); }},
                new PointSale() {{ setId(2L); setName(PUNTOS_VENTA.get(1)); }}
        ));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            costoPuntosServiceImpl.calculateMinPath(1L, 3L);
        });

        assertEquals("Punto/s de venta no encontrado/s", exception.getMessage());
    }

    /**
     * Verifies that {@code calcularCostoTotalRuta} returns correct total cost of a path.
     */
    @Test
    void calculate() {
        List<Long> ruta = Arrays.asList(1L, 2L); // Cost 2.0 between 1 and 2

        double costoTotal = costoPuntosServiceImpl.calculateTotalRouteCost(ruta);

        assertEquals(2.0, costoTotal);
    }

    /**
     * Verifies that {@code calcularCostoTotalRuta} throws exception when cost is missing in cache.
     */
    @Test
    void calculateTotalRouteCost_ThrowsException_WhenKeyNotInCache() {
        List<Long> rutaInvalida = Arrays.asList(2L, 3L);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            costoPuntosServiceImpl.calculateTotalRouteCost(rutaInvalida);
        });

        assertEquals("Falta costo entre 2 y 3", exception.getMessage());
    }

    /**
     * Verifies that {@code eliminarCostosRelacionadosA} removes all costs linked to a sales point.
     */
    @Test
    void deleteRelatedCostsTo_DeletesFromRepository() {
        Long id = 2L;

        CostPoints costo1 = new CostPoints(); costo1.setIdA(1L); costo1.setIdB(2L); costo1.setCost(2.0);
        CostPoints costo2 = new CostPoints(); costo2.setIdA(2L); costo2.setIdB(3L); costo2.setCost(2.5);
        CostPoints costo3 = new CostPoints(); costo3.setIdA(4L); costo3.setIdB(5L); costo3.setCost(3.0); // unrelated

        when(costRepository.findAll()).thenReturn(Arrays.asList(costo1, costo2, costo3));

        costoPuntosServiceImpl.deleteRelatedCostsTo(id);

        verify(costRepository).delete(costo1);
        verify(costRepository).delete(costo2);
        verify(costRepository, never()).delete(costo3);
    }

    /**
     * Verifies that {@code calcularRutaMinima} returns the shortest path between two points.
     */
    @Test
    void calculateMinPath_ReturnsCorrectPath() {
        // Arrange
        Long puntoA = 1L;
        Long puntoB = 4L;

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
        List<Long> ruta = costoPuntosServiceImpl.calculateMinPath(puntoA, puntoB);

        // Assert
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L), ruta);
    }

    /**
     * Verifies that {@code getCostosDesdePunto} works even if the point is the destination (idB).
     */
    @Test
    void getCostsFromPoint_ReturnsCostoCuandoEsId2() {
        // Arrange
        Long idA = 2L;
        Long idRelacionado = 1L;

        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(Arrays.asList(
                new PointSale() {{ setId(idA); setName("Punto 2"); }},
                new PointSale() {{ setId(idRelacionado); setName("Punto 1"); }}
        ));

        costoPuntosServiceImpl.getCache().clear();
        costoPuntosServiceImpl.addCostPoints(idRelacionado, idA, 5.5); // creates key "1-2"

        // Act
        List<CostPointsResponse> costos = costoPuntosServiceImpl.getCostsFromPoint(idA);

        // Assert
        assertEquals(1, costos.size());
        assertEquals(idA, costos.get(0).getIdA());
        assertEquals(idRelacionado, costos.get(0).getIdB());
        assertEquals(5.5, costos.get(0).getCost());
        assertEquals("Punto 1", costos.get(0).getPointNameB());
    }

    /**
     * Verifies that {@code saveCostoToDB} updates the cost if it already exists in the database.
     */
    @Test
    void saveCostoToDB_actualizaCostExistente() {
        // Arrange
        Long idA = 1L;
        Long idB = 2L;
        Double nuevoCosto = 15.0;
        Long menor = Math.min(idA, idB);
        Long mayor = Math.max(idA, idB);

        CostPoints existente = new CostPoints();
        existente.setIdA(menor);
        existente.setIdB(mayor);
        existente.setCost(10.0);

        when(costRepository.findByIdAAndIdB(menor, mayor)).thenReturn(Optional.of(existente));

        // Act
        when(pointSaleService.getAllPointSale()).thenReturn(List.of(
                new PointSale(idA, "A"),
                new PointSale(idB, "B")
        ));

        costoPuntosServiceImpl.addCostPoints(idA, idB, nuevoCosto);

        // Assert
        assertEquals(nuevoCosto, existente.getCost());
        verify(costRepository).save(existente);
    }
}