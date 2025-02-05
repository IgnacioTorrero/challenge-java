package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.service.AcreditacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AcreditacionControllerTest {

    @Mock
    private AcreditacionService acreditacionService;

    @InjectMocks
    private AcreditacionController acreditacionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void recibirAcreditacion_ReturnsAcreditacion() {
        Acreditacion acreditacion = new Acreditacion();
        acreditacion.setId(ID_PUNTO_VENTA);
        acreditacion.setImporte(IMPORTE);
        acreditacion.setIdPuntoVenta(ID_PUNTO_VENTA);

        when(acreditacionService.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA)).thenReturn(acreditacion);
        ResponseEntity<Acreditacion> response = acreditacionController.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA);

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(acreditacion, response.getBody());
    }

    @Test
    void obtenerAcreditaciones_ReturnIterableOfAcreditaciones() {
        Acreditacion acreditacion1 = new Acreditacion();
        acreditacion1.setId(ID_PUNTO_VENTA);
        acreditacion1.setImporte(IMPORTE);

        Acreditacion acreditacion2 = new Acreditacion();
        acreditacion2.setId(ID_PUNTO_VENTA2);
        acreditacion2.setImporte(IMPORTE2);

        Iterable<Acreditacion> acreditaciones = Arrays.asList(acreditacion1, acreditacion2);
        when(acreditacionService.obtenerAcreditaciones()).thenReturn(acreditaciones);
        ResponseEntity<Iterable<Acreditacion>> response = acreditacionController.obtenerAcreditaciones();

        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(acreditaciones, response.getBody());
    }
}
