package com.proyecto.challengejava.controller;

import com.proyecto.challengejava.dto.CostoPuntosRequest;
import com.proyecto.challengejava.dto.CostoPuntosResponse;
import com.proyecto.challengejava.dto.RutaCostoMinimoResponse;
import com.proyecto.challengejava.hateoas.CostoPuntosModelAssembler;
import com.proyecto.challengejava.hateoas.RutaCostoMinimoModelAssembler;
import com.proyecto.challengejava.service.CostoPuntosServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CostoPuntosControllerTest {

    @Mock
    private CostoPuntosServiceImpl service;

    @Mock
    private CostoPuntosModelAssembler assembler;

    @Mock
    private RutaCostoMinimoModelAssembler rutaAssembler;

    @InjectMocks
    private CostoPuntosController controller;

    private final CostoPuntosRequest request = new CostoPuntosRequest(ID_PUNTO_VENTA, ID_PUNTO_VENTA2);

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
    void getCostosDesdePunto_ReturnsCollectionModelOfCostos() {
        // Arrange
        List<CostoPuntosResponse> costos = Arrays.asList(
                new CostoPuntosResponse(1L, 2L, 100.0, "GBA_1"),
                new CostoPuntosResponse(1L, 3L, 150.0, "GBA_2")
        );

        when(service.getCostosDesdePunto(ID_PUNTO_VENTA)).thenReturn(costos);
        when(assembler.toModel(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<CollectionModel<CostoPuntosResponse>> response = controller.getCostosDesdePunto(ID_PUNTO_VENTA);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());

        CollectionModel<CostoPuntosResponse> body = response.getBody();
        assertNotNull(body);
        assertEquals(costos.size(), body.getContent().size());

        for (CostoPuntosResponse esperado : costos) {
            assertTrue(body.getContent().stream().anyMatch(item ->
                    item != null &&
                            item.getIdA().equals(esperado.getIdA()) &&
                            item.getIdB().equals(esperado.getIdB()) &&
                            item.getCosto().equals(esperado.getCosto()) &&
                            item.getNombrePuntoB().equals(esperado.getNombrePuntoB())
            ));
        }

        verify(service, times(1)).getCostosDesdePunto(ID_PUNTO_VENTA);
    }

    @Test
    void calcularCostoMinimo_ReturnsRutaCostoMinimoResponse() {
        List<Long> ruta = Arrays.asList(1L, 2L, 3L);
        Double costoTotal = 25.0;
        RutaCostoMinimoResponse original = new RutaCostoMinimoResponse(ruta, costoTotal);

        RutaCostoMinimoResponse responseConLinks = new RutaCostoMinimoResponse(ruta, costoTotal);
        responseConLinks.add(linkTo(methodOn(CostoPuntosController.class).getCostosDesdePunto(1L)).withRel("ver-costos-desde-1"));
        responseConLinks.add(linkTo(methodOn(CostoPuntosController.class).getCostosDesdePunto(2L)).withRel("ver-costos-desde-2"));
        responseConLinks.add(linkTo(methodOn(CostoPuntosController.class).getCostosDesdePunto(3L)).withRel("ver-costos-desde-3"));
        responseConLinks.add(linkTo(methodOn(CostoPuntosController.class).calcularCostoMinimo(new CostoPuntosRequest(1L, 3L))).withRel("recalcular-ruta"));

        when(service.calcularRutaMinima(anyLong(), anyLong())).thenReturn(ruta);
        when(service.calcularCostoTotalRuta(anyList())).thenReturn(costoTotal);
        when(rutaAssembler.toModel(any())).thenReturn(responseConLinks);

        // Act
        CostoPuntosRequest request = new CostoPuntosRequest(1L, 3L);
        ResponseEntity<RutaCostoMinimoResponse> response = controller.calcularCostoMinimo(request);

        // Assert
        assertEquals(SUCCESS_RESPONSE, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(ruta, response.getBody().getRuta());
        assertEquals(costoTotal, response.getBody().getCostoTotal());

        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-1"));
        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-2"));
        assertTrue(response.getBody().getLinks().hasLink("ver-costos-desde-3"));
        assertTrue(response.getBody().getLinks().hasLink("recalcular-ruta"));

        verify(service, times(1)).calcularRutaMinima(anyLong(), anyLong());
        verify(service, times(1)).calcularCostoTotalRuta(anyList());
        verify(rutaAssembler, times(1)).toModel(any());
    }

    @Test
    void addCostoPuntos_SameId_ThrowsException() {
        // Arrange
        Long id = 5L;
        CostoPuntosRequest requestConIdsIguales = new CostoPuntosRequest(id, id);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.addCostoPuntos(requestConIdsIguales, 100.0);
        });

        assertEquals(INVALID_ID_EXCEPTION, exception.getMessage());
    }
}
