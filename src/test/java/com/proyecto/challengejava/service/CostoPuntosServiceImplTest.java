package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CostoPuntosServiceImplTest {

    @Mock
    private PuntoVentaServiceImpl puntoVentaServiceImpl;

    private CostoPuntosServiceImpl costoPuntosServiceImpl;

    @BeforeEach
    void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);

        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(Arrays.asList(
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
        costoPuntosServiceImpl = new CostoPuntosServiceImpl(puntoVentaServiceImpl);
    }

    @Test
    void addCostoPuntos_ReturnsOk() {
        costoPuntosServiceImpl.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);

        List<CostoPuntos> costos = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

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

        List<CostoPuntos> costos = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

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
    void cargarCostosIniciales_ReturnsOk() {
        costoPuntosServiceImpl.cargarCostosIniciales();

        List<CostoPuntos> costosDesdePunto1 = costoPuntosServiceImpl.getCostosDesdePunto(ID_PUNTO_VENTA);

        assertFalse(costosDesdePunto1.isEmpty());
        assertEquals(3, costosDesdePunto1.size());
    }

    @Test
    void cargarCostosIniciales_ThrowsExceptionIllegalArgumentException() throws NoSuchMethodException {
        Method method = CostoPuntosServiceImpl.class.getDeclaredMethod(METHOD_AGREGAR_COSTO_INICIAL, Long.class, Long.class, Double.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () ->
                method.invoke(costoPuntosServiceImpl, INVALID_ID, INVALID_ID2, null)
        );
        assertNull(exception.getMessage());
    }
}