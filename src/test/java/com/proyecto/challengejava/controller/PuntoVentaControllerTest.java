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

        punto1.setId(ID_PUNTO_VENTA);
        punto1.setNombre(PUNTO_VENTA_1);
        punto2.setId(ID_PUNTO_VENTA2);
        punto2.setNombre(PUNTO_VENTA_2);
    }

    @Test
    void getAllPuntosVenta_ReturnsListOfPuntosVenta() {
        List<PuntoVenta> mockPuntosVenta = Arrays.asList(punto1, punto2);
        when(service.getAllPuntosVenta()).thenReturn(mockPuntosVenta);

        ResponseEntity<List<PuntoVenta>> response = controller.getAllPuntos();

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(mockPuntosVenta, response.getBody());
        verify(service, times(1)).getAllPuntosVenta();
    }

    @Test
    void addPuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.addPunto(ID_PUNTO_VENTA5, PUNTO_VENTA_3);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).addPuntoVenta(ID_PUNTO_VENTA5, PUNTO_VENTA_3);
    }

    @Test
    void updatePuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.updatePunto(ID_PUNTO_VENTA, PUNTO_VENTA_4);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).updatePuntoVenta(ID_PUNTO_VENTA, PUNTO_VENTA_4);
    }

    @Test
    void deletePuntoVenta_ReturnsResponseOk() {
        ResponseEntity<Void> response = controller.deletePunto(ID_PUNTO_VENTA4);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        verify(service, times(1)).deletePuntoVenta(ID_PUNTO_VENTA4);
    }
}
