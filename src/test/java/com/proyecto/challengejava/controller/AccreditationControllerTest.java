package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.AccreditationRequest;
import com.proyecto.challengejava.dto.AccreditationResponse;
import com.proyecto.challengejava.entity.Accreditation;
import com.proyecto.challengejava.hateoas.AccreditationModelAssembler;
import com.proyecto.challengejava.service.AccreditationServiceImpl;
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
    private AccreditationServiceImpl acreditacionServiceImpl;

    @Mock
    private AccreditationModelAssembler acreditacionAssembler;

    @InjectMocks
    private AccreditationController accreditationController;

    private final AccreditationRequest request = new AccreditationRequest();

    /**
     * Initializes the mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request.setAmount(IMPORTE);
        request.setIdPointSale(ID_PUNTO_VENTA);
    }

    /**
     * Test to verify that the {@code recibirAcreditacion} method
     * returns a response with the expected data.
     */
    @Test
    void receiveAccreditationResponse() {
        // Arrange: simulate the accreditation returned by the service
        Accreditation accreditation = new Accreditation();
        accreditation.setId(ID_PUNTO_VENTA);
        accreditation.setAmount(IMPORTE);
        accreditation.setIdPointSale(ID_PUNTO_VENTA);

        AccreditationRequest request = new AccreditationRequest();
        request.setAmount(IMPORTE);
        request.setIdPointSale(ID_PUNTO_VENTA);

        AccreditationResponse responseSinLinks = new AccreditationResponse(
                accreditation.getId(),
                accreditation.getAmount(),
                accreditation.getIdPointSale(),
                accreditation.getPointSaleName(),
                accreditation.getDateReception()
        );

        when(acreditacionServiceImpl.receiveAccreditation(IMPORTE, ID_PUNTO_VENTA)).thenReturn(accreditation);
        when(acreditacionAssembler.toModel(responseSinLinks)).thenReturn(responseSinLinks);

        // Act: call the controller
        ResponseEntity<AccreditationResponse> response = accreditationController.receiveAccreditation(request);

        // Assert: verify the response
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertEquals(responseSinLinks.getAmount(), response.getBody().getAmount());
        assertEquals(responseSinLinks.getIdPointSale(), response.getBody().getIdPointSale());
    }

    /**
     * Test to verify that the {@code obtenerAcreditaciones} method
     * returns a list of accreditations correctly transformed.
     */
    @Test
    void getAccreditations_ReturnsCollectionModelOfAcreditacionResponses() {
        // Arrange: simulate two accreditations returned by the service
        Accreditation accreditation1 = new Accreditation();
        accreditation1.setId(ID_PUNTO_VENTA);
        accreditation1.setAmount(IMPORTE);

        Accreditation accreditation2 = new Accreditation();
        accreditation2.setId(ID_PUNTO_VENTA2);
        accreditation2.setAmount(IMPORTE2);

        Iterable<Accreditation> acreditaciones = Arrays.asList(accreditation1, accreditation2);

        AccreditationResponse response1 = new AccreditationResponse(
                accreditation1.getId(), accreditation1.getAmount(),
                accreditation1.getIdPointSale(), accreditation1.getPointSaleName(),
                accreditation1.getDateReception()
        );
        AccreditationResponse response2 = new AccreditationResponse(
                accreditation2.getId(), accreditation2.getAmount(),
                accreditation2.getIdPointSale(), accreditation2.getPointSaleName(),
                accreditation2.getDateReception()
        );

        when(acreditacionServiceImpl.getAccreditations()).thenReturn(acreditaciones);
        when(acreditacionAssembler.toModel(response1)).thenReturn(response1);
        when(acreditacionAssembler.toModel(response2)).thenReturn(response2);

        // Act: call the controller
        ResponseEntity<?> response = accreditationController.getAccreditations();

        // Assert: verify the response
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}