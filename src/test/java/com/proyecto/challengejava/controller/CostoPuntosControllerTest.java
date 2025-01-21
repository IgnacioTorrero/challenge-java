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
        Long idA = 1L;
        Long idB = 2L;
        Double costo = 100.0;

        ResponseEntity<Void> response = controller.addCostoPuntos(idA, idB, costo);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).addCostoPuntos(idA, idB, costo);
    }

    @Test
    void removeCostoPuntos_ReturnsOk() {
        Long idA = 1L;
        Long idB = 2L;

        ResponseEntity<Void> response = controller.removeCostoPuntos(idA, idB);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).removeCostoPuntos(idA, idB);
    }

    @Test
    void getCostosDesdePunto_ReturnsListOfCostos() {
        Long idA = 1L;
        List<CostoPuntos> mockCostos = Arrays.asList(
                new CostoPuntos(1L, 2L, 100.0, null),
                new CostoPuntos(1L, 3L, 150.0, null)
        );
        when(service.getCostosDesdePunto(idA)).thenReturn(mockCostos);

        ResponseEntity<List<CostoPuntos>> response = controller.getCostosDesdePunto(idA);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCostos, response.getBody());
        verify(service, times(1)).getCostosDesdePunto(idA);
    }
}
