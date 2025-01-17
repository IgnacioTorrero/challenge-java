package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.service.PuntoVentaService;
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

public class PuntoVentaControllerTest {

    @Mock
    private PuntoVentaService service;

    @InjectMocks
    private PuntoVentaController controller;

    private final PuntoVenta punto1 = new PuntoVenta();
    private final PuntoVenta punto2 = new PuntoVenta();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        punto1.setId(1L);
        punto1.setNombre(PUNTO_VENTA_1);
        punto2.setId(2L);
        punto2.setNombre(PUNTO_VENTA_2);
    }

    @Test
    void getAllPuntosVenta_ReturnsListOfPuntosVenta() {
        List<PuntoVenta> mockPuntosVenta = Arrays.asList(punto1, punto2);
        when(service.getAllPuntosVenta()).thenReturn(mockPuntosVenta);

        ResponseEntity<List<PuntoVenta>> response = controller.getAllPuntos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPuntosVenta, response.getBody());
        verify(service, times(1)).getAllPuntosVenta();
    }

    @Test
    void addPuntoVenta_ReturnsResponseOk() {
        Long id = 11L;

        ResponseEntity<Void> response = controller.addPunto(id, PUNTO_VENTA_3);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).addPuntoVenta(id, PUNTO_VENTA_3);
    }

    @Test
    void updatePuntoVenta_ReturnsResponseOk() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.updatePunto(id, PUNTO_VENTA_4);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).updatePuntoVenta(id, PUNTO_VENTA_4);
    }

    @Test
    void deletePuntoVenta_ReturnsResponseOk() {
        Long id = 10L;

        ResponseEntity<Void> response = controller.deletePunto(id);

        assertEquals(200, response.getStatusCodeValue());
        verify(service, times(1)).deletePuntoVenta(id);
    }
}
