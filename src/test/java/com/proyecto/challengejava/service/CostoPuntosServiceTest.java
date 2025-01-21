package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_NOT_FOUND;
import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CostoPuntosServiceTest {

    @Mock
    private PuntoVentaService puntoVentaService;

    private CostoPuntosService costoPuntosService;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);

        when(puntoVentaService.getAllPuntosVenta()).thenReturn(Arrays.asList(
                new PuntoVenta() {{
                    setId(1L);
                    setNombre(PUNTOS_VENTA.get(0));
                }},
                new PuntoVenta() {{
                    setId(2L);
                    setNombre(PUNTOS_VENTA.get(1));
                }},
                new PuntoVenta() {{
                    setId(3L);
                    setNombre(PUNTOS_VENTA.get(2));
                }},
                new PuntoVenta() {{
                    setId(4L);
                    setNombre(PUNTOS_VENTA.get(3));
                }},
                new PuntoVenta() {{
                    setId(5L);
                    setNombre(PUNTOS_VENTA.get(4));
                }},
                new PuntoVenta() {{
                    setId(6L);
                    setNombre(PUNTOS_VENTA.get(5));
                }},
                new PuntoVenta() {{
                    setId(7L);
                    setNombre(PUNTOS_VENTA.get(6));
                }},
                new PuntoVenta() {{
                    setId(8L);
                    setNombre(PUNTOS_VENTA.get(7));
                }},
                new PuntoVenta() {{
                    setId(9L);
                    setNombre(PUNTOS_VENTA.get(8));
                }},
                new PuntoVenta() {{
                    setId(10L);
                    setNombre(PUNTOS_VENTA.get(9));
                }}
        ));
        costoPuntosService = new CostoPuntosService(puntoVentaService);
    }

    @Test
    void addCostoPuntos_ReturnsOk() {
        costoPuntosService.addCostoPuntos(1L, 2L, 100.0);

        List<CostoPuntos> costos = costoPuntosService.getCostosDesdePunto(1L);

        assertEquals(3, costos.size());
        assertEquals(100.0, costos.get(0).getCosto());
    }

    @Test
    void addCostoPuntosLessThanZero_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosService.addCostoPuntos(1L, 2L, INVALID_COSTO)
        );
        assertEquals(COSTO_PUNTOS_LESS_THAN_ZERO, exception.getMessage());
    }

    @Test
    void addCostoPuntosNotFound_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosService.addCostoPuntos(INVALID_ID, INVALID_ID2, 100.0)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void removeCostoPuntosPuntos_ReturnsOk() {
        costoPuntosService.removeCostoPuntos(1L, 2L);
        double costoAhora = costoPuntosService.getCostosDesdePunto(1L).get(0).getCosto();
        assertEquals(0.0, costoAhora);
    }

    @Test
    void getCostosDesdePunto_ReturnsCostoPuntos() {
        costoPuntosService.addCostoPuntos(1L, 2L, 100.0);
        costoPuntosService.addCostoPuntos(1L, 3L, 150.0);

        List<CostoPuntos> costos = costoPuntosService.getCostosDesdePunto(1L);

        assertEquals(3, costos.size());
        assertEquals(100.0, costos.get(0).getCosto());
        assertEquals(150.0, costos.get(1).getCosto());
    }

    @Test
    void getCostosDesdePunto_ThrowsExceptionIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosService.getCostosDesdePunto(INVALID_ID)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void removeCostoPuntos_ThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                costoPuntosService.removeCostoPuntos(INVALID_ID, INVALID_ID2)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void cargarCostosIniciales_ReturnsOk() {
        costoPuntosService.cargarCostosIniciales();

        List<CostoPuntos> costosDesdePunto1 = costoPuntosService.getCostosDesdePunto(1L);

        assertFalse(costosDesdePunto1.isEmpty());
        assertEquals(3, costosDesdePunto1.size());
    }

    @Test
    void cargarCostosIniciales_ThrowsExceptionIllegalArgumentException() throws NoSuchMethodException {
        Method method = CostoPuntosService.class.getDeclaredMethod(METHOD_AGREGAR_COSTO_INICIAL, Long.class, Long.class, Double.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () ->
                method.invoke(costoPuntosService, INVALID_ID, INVALID_ID2, null)
        );
        assertNull(exception.getMessage());
    }
}