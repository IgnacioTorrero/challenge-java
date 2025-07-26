package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AccreditationRequest;
import com.proyecto.challengejava.dto.AccreditationResponse;
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

import static com.proyecto.challengejava.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link AccreditationController}.
 * Verifies correct behavior of endpoints related to accreditations.
 */
public class AccreditationControllerTest {

    @Mock
    private AcreditacionServiceImpl acreditacionServiceImpl;

    @Mock
    private AcreditacionModelAssembler acreditacionAssembler;

    @InjectMocks
    private AccreditationController accreditationController;

    private final AccreditationRequest request = new AccreditationRequest();

    /**
     * Initializes the mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request.setImporte(IMPORTE);
        request.setIdPuntoVenta(ID_PUNTO_VENTA);
    }

    /**
     * Test to verify that the {@code recibirAcreditacion} method
     * returns a response with the expected data.
     */
    @Test
    void recibirAcreditacion_ReturnsAcreditacionResponse() {
        // Arrange: simulate the accreditation returned by the service
        Acreditacion acreditacion = new Acreditacion();
        acreditacion.setId(ID_PUNTO_VENTA);
        acreditacion.setImporte(IMPORTE);
        acreditacion.setIdPuntoVenta(ID_PUNTO_VENTA);

        AccreditationRequest request = new AccreditationRequest();
        request.setImporte(IMPORTE);
        request.setIdPuntoVenta(ID_PUNTO_VENTA);

        AccreditationResponse responseSinLinks = new AccreditationResponse(
                acreditacion.getId(),
                acreditacion.getImporte(),
                acreditacion.getIdPuntoVenta(),
                acreditacion.getNombrePuntoVenta(),
                acreditacion.getFechaRecepcion()
        );

        when(acreditacionServiceImpl.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA)).thenReturn(acreditacion);
        when(acreditacionAssembler.toModel(responseSinLinks)).thenReturn(responseSinLinks);

        // Act: call the controller
        ResponseEntity<AccreditationResponse> response = accreditationController.recibirAcreditacion(request);

        // Assert: verify the response
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(responseSinLinks.getImporte(), response.getBody().getImporte());
        assertEquals(responseSinLinks.getIdPuntoVenta(), response.getBody().getIdPuntoVenta());
    }

    /**
     * Test to verify that the {@code obtenerAcreditaciones} method
     * returns a list of accreditations correctly transformed.
     */
    @Test
    void obtenerAcreditaciones_ReturnsCollectionModelOfAcreditacionResponses() {
        // Arrange: simulate two accreditations returned by the service
        Acreditacion acreditacion1 = new Acreditacion();
        acreditacion1.setId(ID_PUNTO_VENTA);
        acreditacion1.setImporte(IMPORTE);

        Acreditacion acreditacion2 = new Acreditacion();
        acreditacion2.setId(ID_PUNTO_VENTA2);
        acreditacion2.setImporte(IMPORTE2);

        Iterable<Acreditacion> acreditaciones = Arrays.asList(acreditacion1, acreditacion2);

        AccreditationResponse response1 = new AccreditationResponse(
                acreditacion1.getId(), acreditacion1.getImporte(),
                acreditacion1.getIdPuntoVenta(), acreditacion1.getNombrePuntoVenta(),
                acreditacion1.getFechaRecepcion()
        );
        AccreditationResponse response2 = new AccreditationResponse(
                acreditacion2.getId(), acreditacion2.getImporte(),
                acreditacion2.getIdPuntoVenta(), acreditacion2.getNombrePuntoVenta(),
                acreditacion2.getFechaRecepcion()
        );

        when(acreditacionServiceImpl.obtenerAcreditaciones()).thenReturn(acreditaciones);
        when(acreditacionAssembler.toModel(response1)).thenReturn(response1);
        when(acreditacionAssembler.toModel(response2)).thenReturn(response2);

        // Act: call the controller
        ResponseEntity<?> response = accreditationController.obtenerAcreditaciones();

        // Assert: verify the response
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}