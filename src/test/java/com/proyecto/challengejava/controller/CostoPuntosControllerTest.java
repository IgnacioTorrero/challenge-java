package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostoPuntosRequest;
import com.proyecto.challengejava.dto.RutaCostoMinimoResponse;
import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.service.CostoPuntosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CostoPuntosControllerTest {

    @Mock
    private CostoPuntosServiceImpl service;

    @InjectMocks
    private CostoPuntosController controller;

    private final CostoPuntosRequest request = new CostoPuntosRequest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request.setIdA(ID_PUNTO_VENTA5);
        request.setIdB(ID_PUNTO_VENTA2);
    }

    @Test
    void addCostoPuntos_ReturnsOk() {
        ResponseEntity<Void> response = controller.addCostoPuntos(request, IMPORTE);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).addCostoPuntos(ID_PUNTO_VENTA5, ID_PUNTO_VENTA2, IMPORTE);
    }

    @Test
    void removeCostoPuntos_ReturnsOk() {
        ResponseEntity<Void> response = controller.removeCostoPuntos(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).removeCostoPuntos(ID_PUNTO_VENTA5, ID_PUNTO_VENTA2);
    }

    @Test
    void getCostosDesdePunto_ReturnsListOfCostos() {
        List<CostoPuntos> costos = Arrays.asList(
                new CostoPuntos(1L, 2L, 100.0, null),
                new CostoPuntos(1L, 3L, 150.0, null)
        );
        when(service.getCostosDesdePunto(ID_PUNTO_VENTA)).thenReturn(costos);

        ResponseEntity<List<CostoPuntos>> response = controller.getCostosDesdePunto(ID_PUNTO_VENTA);
        
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(costos, response.getBody());
        verify(service, times(1)).getCostosDesdePunto(ID_PUNTO_VENTA);
    }

    @Test
    void calcularCostoMinimo_ReturnsRutaCostoMinimoResponse() {
        List<Long> ruta = Arrays.asList(1L, 2L, 3L); // Ruta esperada
        Double costoTotal = 25.0;

        // Mockeamos los métodos de servicio
        when(service.calcularRutaMinima(anyLong(), anyLong())).thenReturn(ruta);
        when(service.calcularCostoTotalRuta(anyList())).thenReturn(costoTotal);

        ResponseEntity<RutaCostoMinimoResponse> response = controller.calcularCostoMinimo(request);

        // Verificar resultados
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(ruta, response.getBody().getRuta());
        assertEquals(costoTotal, response.getBody().getCostoTotal());

        // Verificar que los métodos mockeados fueron llamados
        verify(service, times(1)).calcularRutaMinima(anyLong(), anyLong());
        verify(service, times(1)).calcularCostoTotalRuta(anyList());
    }
}
