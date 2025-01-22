package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.CostoPuntos;
import com.proyecto.challengejava.service.CostoPuntosService;
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
    private CostoPuntosService service;

    @InjectMocks
    private CostoPuntosController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCostoPuntos_ReturnsOk() {
        ResponseEntity<Void> response = controller.addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).addCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2, IMPORTE);
    }

    @Test
    void removeCostoPuntos_ReturnsOk() {
        ResponseEntity<Void> response = controller.removeCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).removeCostoPuntos(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);
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
}
