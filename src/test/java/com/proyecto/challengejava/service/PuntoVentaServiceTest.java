package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static org.junit.jupiter.api.Assertions.*;

public class PuntoVentaServiceTest {

    private PuntoVentaService service;

    @BeforeEach
    void setUp() {
        service = new PuntoVentaService();
    }

    @Test
    void getAllPuntosVenta_ReturnListOfPuntosVenta() {
        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();

        assertNotNull(puntosVenta);
        assertEquals(10, puntosVenta.size());
        assertEquals(1L, puntosVenta.get(0).getId());
        assertNotNull(puntosVenta.get(0).getNombre());
    }

    @Test
    void addPuntoVenta_AddsNewPuntoVenta() {
        Long id = 11L;

        service.addPuntoVenta(id, PUNTO_VENTA_3);
        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();

        assertEquals(11, puntosVenta.size());
        PuntoVenta addedPunto = puntosVenta.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        assertNotNull(addedPunto);
        assertEquals(PUNTO_VENTA_3, addedPunto.getNombre());
    }

    @Test
    void updatePuntoVenta_UpdatesExistingPuntoVenta() {
        Long id = 2L;

        service.updatePuntoVenta(id, PUNTO_VENTA_5);
        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();

        PuntoVenta updatedPunto = puntosVenta.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedPunto);
        assertEquals(PUNTO_VENTA_5, updatedPunto.getNombre());
    }

    @Test
    void updatePuntoVenta_ThrowsIllegalArgumentException() {
        Long id = 99L;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.updatePuntoVenta(id, PUNTO_VENTA_6)
        );
        assertEquals(PUNTO_VENTA_NOT_FOUND, exception.getMessage());
    }

    @Test
    void deletePuntoVenta_RemovesPuntoVenta() {
        Long id = 10L;

        service.deletePuntoVenta(id);
        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();

        assertEquals(9, puntosVenta.size());
        PuntoVenta deletedPunto = puntosVenta.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        assertNull(deletedPunto);
    }
}
