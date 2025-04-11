package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AcreditacionRequest;
import com.proyecto.challengejava.dto.AcreditacionResponse;
import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.hateoas.AcreditacionModelAssembler;
import com.proyecto.challengejava.service.AcreditacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AcreditacionControllerTest {

    @Mock
    private AcreditacionServiceImpl acreditacionServiceImpl;

    @Mock
    private AcreditacionModelAssembler acreditacionAssembler;

    @InjectMocks
    private AcreditacionController acreditacionController;

    private final AcreditacionRequest request = new AcreditacionRequest();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request.setImporte(IMPORTE);
        request.setIdPuntoVenta(ID_PUNTO_VENTA);
    }

    @Test
    void recibirAcreditacion_ReturnsAcreditacionResponse() {
        Acreditacion acreditacion = new Acreditacion();
        acreditacion.setId(ID_PUNTO_VENTA);
        acreditacion.setImporte(IMPORTE);
        acreditacion.setIdPuntoVenta(ID_PUNTO_VENTA);

        AcreditacionRequest request = new AcreditacionRequest();
        request.setImporte(IMPORTE);
        request.setIdPuntoVenta(ID_PUNTO_VENTA);

        AcreditacionResponse responseSinLinks = new AcreditacionResponse(
                acreditacion.getId(),
                acreditacion.getImporte(),
                acreditacion.getIdPuntoVenta(),
                acreditacion.getNombrePuntoVenta(),
                acreditacion.getFechaRecepcion()
        );

        when(acreditacionServiceImpl.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA)).thenReturn(acreditacion);
        when(acreditacionAssembler.toModel(responseSinLinks)).thenReturn(responseSinLinks);

        // Act
        ResponseEntity<AcreditacionResponse> response = acreditacionController.recibirAcreditacion(request);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(responseSinLinks.getImporte(), response.getBody().getImporte());
        assertEquals(responseSinLinks.getIdPuntoVenta(), response.getBody().getIdPuntoVenta());
    }

    @Test
    void obtenerAcreditaciones_ReturnsCollectionModelOfAcreditacionResponses() {
        Acreditacion acreditacion1 = new Acreditacion();
        acreditacion1.setId(ID_PUNTO_VENTA);
        acreditacion1.setImporte(IMPORTE);

        Acreditacion acreditacion2 = new Acreditacion();
        acreditacion2.setId(ID_PUNTO_VENTA2);
        acreditacion2.setImporte(IMPORTE2);

        Iterable<Acreditacion> acreditaciones = Arrays.asList(acreditacion1, acreditacion2);

        AcreditacionResponse response1 = new AcreditacionResponse(
                acreditacion1.getId(), acreditacion1.getImporte(),
                acreditacion1.getIdPuntoVenta(), acreditacion1.getNombrePuntoVenta(),
                acreditacion1.getFechaRecepcion()
        );
        AcreditacionResponse response2 = new AcreditacionResponse(
                acreditacion2.getId(), acreditacion2.getImporte(),
                acreditacion2.getIdPuntoVenta(), acreditacion2.getNombrePuntoVenta(),
                acreditacion2.getFechaRecepcion()
        );

        when(acreditacionServiceImpl.obtenerAcreditaciones()).thenReturn(acreditaciones);
        when(acreditacionAssembler.toModel(response1)).thenReturn(response1);
        when(acreditacionAssembler.toModel(response2)).thenReturn(response2);

        // Act
        ResponseEntity<?> response = acreditacionController.obtenerAcreditaciones();

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
