package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Acreditacion;
import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.exception.PuntoVentaNotFoundException;
import com.proyecto.challengejava.repository.AcreditacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.proyecto.challengejava.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link AcreditacionServiceImpl}.
 * Validates logic related to accreditation creation and retrieval.
 */
public class AcreditacionServiceImplTest {

    @Mock
    private AcreditacionRepository acreditacionRepository;

    @Mock
    private PuntoVentaServiceImpl puntoVentaServiceImpl;

    @InjectMocks
    private AcreditacionServiceImpl acreditacionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifies that {@code recibirAcreditacion} correctly saves and returns an accreditation
     * when the sales point exists.
     */
    @Test
    void recibirAcreditacion_SavesAndReturnsAcreditacion() {
        PuntoVenta puntoVenta = new PuntoVenta();
        puntoVenta.setId(ID_PUNTO_VENTA);
        puntoVenta.setNombre(PUNTO_VENTA_1);

        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(List.of(puntoVenta));

        Acreditacion acreditacion = new Acreditacion();
        acreditacion.setId(ID_PUNTO_VENTA);
        acreditacion.setImporte(IMPORTE);
        acreditacion.setIdPuntoVenta(ID_PUNTO_VENTA);
        acreditacion.setNombrePuntoVenta(PUNTO_VENTA_1);
        acreditacion.setFechaRecepcion(LocalDate.now());

        when(acreditacionRepository.save(any(Acreditacion.class))).thenReturn(acreditacion);
        Acreditacion result = acreditacionServiceImpl.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA);

        assertNotNull(result);
        assertEquals(IMPORTE, result.getImporte());
        assertEquals(ID_PUNTO_VENTA, result.getIdPuntoVenta());
        assertEquals(PUNTO_VENTA_1, result.getNombrePuntoVenta());
        assertEquals(LocalDate.now(), result.getFechaRecepcion());
        verify(acreditacionRepository, times(1)).save(any(Acreditacion.class));
    }

    /**
     * Verifies that {@code recibirAcreditacion} throws a {@link PuntoVentaNotFoundException}
     * when the sales point does not exist.
     */
    @Test
    void recibirAcreditacion_ThrowsIllegalArgumentException() {
        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(List.of());

        Exception exception = assertThrows(PuntoVentaNotFoundException.class,
                () -> acreditacionServiceImpl.recibirAcreditacion(IMPORTE, INVALID_ID));
        assertEquals(PUNTO_VENTA_NOT_FOUND + ": 99", exception.getMessage());
        verify(acreditacionRepository, never()).save(any(Acreditacion.class));
    }

    /**
     * Verifies that {@code obtenerAcreditaciones} returns all accreditations
     * from the repository.
     */
    @Test
    void obtenerAcreditaciones_ReturnsAllAcreditaciones() {
        Acreditacion acreditacion1 = new Acreditacion();
        acreditacion1.setId(ID_PUNTO_VENTA);
        acreditacion1.setImporte(IMPORTE);

        Acreditacion acreditacion2 = new Acreditacion();
        acreditacion2.setId(ID_PUNTO_VENTA2);
        acreditacion2.setImporte(IMPORTE2);

        when(acreditacionRepository.findAll()).thenReturn(Arrays.asList(acreditacion1, acreditacion2));
        Iterable<Acreditacion> result = acreditacionServiceImpl.obtenerAcreditaciones();

        assertNotNull(result);
        assertTrue(((Iterable<?>) result).iterator().hasNext());
        verify(acreditacionRepository, times(1)).findAll();
    }
}