package com.proyecto.challengejava.service;

import com.proyecto.challengejava.dto.CostoPuntosResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.CostoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CostoPuntosServiceImplTest {

    @Mock
    private PuntoVentaServiceImpl puntoVentaServiceImpl;

    private CostoPuntosServiceImpl costoPuntosServiceImpl;

    @Mock
    private CostoRepository costoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(Arrays.asList(
                new PuntoVenta() {{ setId(1L); setNombre(PUNTOS_VENTA.get(0)); }},
                new PuntoVenta() {{ setId(2L); setNombre(PUNTOS_VENTA.get(1)); }},
                new PuntoVenta() {{ setId(3L); setNombre(PUNTOS_VENTA.get(2)); }},
                new PuntoVenta() {{ setId(4L); setNombre(PUNTOS_VENTA.get(3)); }},
                new PuntoVenta() {{ setId(5L); setNombre(PUNTOS_VENTA.get(4)); }},
                new PuntoVenta() {{ setId(6L); setNombre(PUNTOS_VENTA.get(5)); }},
                new PuntoVenta() {{ setId(7L); setNombre(PUNTOS_VENTA.get(6)); }},
                new PuntoVenta() {{ setId(8L); setNombre(PUNTOS_VENTA.get(7)); }},
                new PuntoVenta() {{ setId(9L); setNombre(PUNTOS_VENTA.get(8)); }},
                new PuntoVenta() {{ setId(10L); setNombre(PUNTOS_VENTA.get(9)); }}
        ));

        when(costoRepository.findAll()).thenReturn(Arrays.asList(
                new CostoPuntos() {{ setIdA(1L); setIdB(2L); setCosto(2.0); }},
                new CostoPuntos() {{ setIdA(1L); setIdB(3L); setCosto(3.0); }},
                new CostoPuntos() {{ setIdA(1L); setIdB(4L); setCosto(4.0); }}
        ));

        costoPuntosServiceImpl = new CostoPuntosServiceImpl(puntoVentaServiceImpl, costoRepository);

        // 👇 Solución mágica
        costoPuntosServiceImpl.cargarCacheDesdeDB();
    }

    @Test
    void addCostoPuntos_ReturnsOk() {
        costoPuntosServiceImpl.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);

        List<CostoPuntosResponse> costos = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

        assertEquals(3, costos.size());
        assertEquals(IMPORTE, costos.get(0).getCosto());
    }

    @Test
    void addCostoPuntosLessThanZero_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, INVALID_COSTO)
        );
        assertEquals(COSTO_PUNTOS_LESS_THAN_ZERO, exception.getMessage());
    }

    @Test
    void addCostoPuntosNotFound_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.addCostoPuntos(INVALID_ID, INVALID_ID2, IMPORTE)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void removeCostoPuntos_ReturnsOk() {
        costoPuntosServiceImpl.removeCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);
        double costoAhora = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA).get(0).getCosto();
        assertEquals(0.0, costoAhora);
    }

    @Test
    void getCostosDesdePunto_ReturnsCostoPuntos() {
        costoPuntosServiceImpl.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);
        costoPuntosServiceImpl.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA3, IMPORTE2);

        List<CostoPuntosResponse> costos = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

        assertEquals(3, costos.size());
        assertEquals(IMPORTE, costos.get(0).getCosto());
        assertEquals(IMPORTE2, costos.get(1).getCosto());
    }

    @Test
    void getCostosDesdePunto_ThrowsExceptionIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosServiceImpl.getCostosDesdePunto(INVALID_ID)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void removeCostoPuntos_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(PuntoVentaNotFoundException.class, () ->
                costoPuntosServiceImpl.removeCostoPuntos(INVALID_ID, INVALID_ID2)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void cargarCacheDesdeDB_ReturnsOk() {
        costoPuntosServiceImpl.cargarCacheDesdeDB();

        List<CostoPuntosResponse> costosDesdePunto1 = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

        assertFalse(costosDesdePunto1.isEmpty());
        assertEquals(3, costosDesdePunto1.size());
    }

    @Test
    void calcularRutaMinima_ThrowsIllegalArgumentException_WhenPuntoVentaDoesNotExist() {
        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(Arrays.asList(
                new PuntoVenta() {{
                    setId(1L);
                    setNombre(PUNTOS_VENTA.get(0));
                }},
                new PuntoVenta() {{
                    setId(2L);
                    setNombre(PUNTOS_VENTA.get(1));
                }}
        ));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            costoPuntosServiceImpl.calcularRutaMinima(1L, 3L);
        });

        assertEquals("Punto/s de venta no encontrado/s", exception.getMessage());
    }

    @Test
    void calcularCostoTotalRuta_ReturnsCorrectCosto() {
        List<Long> ruta = Arrays.asList(1L, 2L, 3L); // 1→2 y 2→3 deben existir en cache

        ruta = Arrays.asList(1L, 2L); // Key 1-2 tiene costo 2.0 según tu setUp

        double costoTotal = costoPuntosServiceImpl.calcularCostoTotalRuta(ruta);

        assertEquals(2.0, costoTotal);
    }

    @Test
    void calcularCostoTotalRuta_ThrowsException_WhenKeyNotInCache() {
        List<Long> rutaInvalida = Arrays.asList(2L, 3L);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            costoPuntosServiceImpl.calcularCostoTotalRuta(rutaInvalida);
        });

        assertEquals("Falta costo entre 2 y 3", exception.getMessage());
    }

    @Test
    void eliminarCostosRelacionadosA_DeletesFromRepository() {
        Long id = 2L;

        CostoPuntos costo1 = new CostoPuntos(); costo1.setIdA(1L); costo1.setIdB(2L); costo1.setCosto(2.0);
        CostoPuntos costo2 = new CostoPuntos(); costo2.setIdA(2L); costo2.setIdB(3L); costo2.setCosto(2.5);
        CostoPuntos costo3 = new CostoPuntos(); costo3.setIdA(4L); costo3.setIdB(5L); costo3.setCosto(3.0); // no relacionado

        when(costoRepository.findAll()).thenReturn(Arrays.asList(costo1, costo2, costo3));

        // Ejecutar
        costoPuntosServiceImpl.eliminarCostosRelacionadosA(id);

        // Verificar llamadas
        verify(costoRepository).delete(costo1);
        verify(costoRepository).delete(costo2);
        verify(costoRepository, never()).delete(costo3);
    }

    @Test
    void calcularRutaMinima_ReturnsCorrectPath() {
        // Arrange
        Long puntoA = 1L;
        Long puntoB = 4L;

        // Simula 4 puntos de venta conectados secuencialmente
        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(Arrays.asList(
                new PuntoVenta() {{ setId(1L); setNombre("P1"); }},
                new PuntoVenta() {{ setId(2L); setNombre("P2"); }},
                new PuntoVenta() {{ setId(3L); setNombre("P3"); }},
                new PuntoVenta() {{ setId(4L); setNombre("P4"); }}
        ));

        // Limpiamos la cache y la llenamos a mano
        costoPuntosServiceImpl.getCache().clear();
        costoPuntosServiceImpl.addCostoPuntos(1L, 2L, 1.0); // 1 → 2
        costoPuntosServiceImpl.addCostoPuntos(2L, 3L, 1.0); // 2 → 3
        costoPuntosServiceImpl.addCostoPuntos(3L, 4L, 1.0); // 3 → 4

        // Act
        List<Long> ruta = costoPuntosServiceImpl.calcularRutaMinima(puntoA, puntoB);

        // Assert
        assertEquals(Arrays.asList(1L, 2L, 3L, 4L), ruta);
    }

    @Test
    void getCostosDesdePunto_ReturnsCostoCuandoEsId2() {
        // Arrange
        Long idA = 2L;
        Long idRelacionado = 1L;

        // Simular puntos válidos
        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(Arrays.asList(
                new PuntoVenta() {{ setId(idA); setNombre("Punto 2"); }},
                new PuntoVenta() {{ setId(idRelacionado); setNombre("Punto 1"); }}
        ));

        costoPuntosServiceImpl.getCache().clear();
        costoPuntosServiceImpl.addCostoPuntos(idRelacionado, idA, 5.5); // crea key "1-2"

        // Act
        List<CostoPuntosResponse> costos = costoPuntosServiceImpl.getCostosDesdePunto(idA);

        // Assert
        assertEquals(1, costos.size());
        assertEquals(idA, costos.get(0).getIdA());
        assertEquals(idRelacionado, costos.get(0).getIdB());
        assertEquals(5.5, costos.get(0).getCosto());
        assertEquals("Punto 1", costos.get(0).getNombrePuntoB());
    }
}