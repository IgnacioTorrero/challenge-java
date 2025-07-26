package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.Accreditation;
import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.exception.PointSaleNotFoundException;
import com.proyecto.challengejava.repository.AccreditationRepository;
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
public class AccreditationServiceImplTest {

    @Mock
    private AccreditationRepository accreditationRepository;

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
        PointSale pointSale = new PointSale();
        pointSale.setId(ID_PUNTO_VENTA);
        pointSale.setNombre(PUNTO_VENTA_1);

        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(List.of(pointSale));

        Accreditation accreditation = new Accreditation();
        accreditation.setId(ID_PUNTO_VENTA);
        accreditation.setImporte(IMPORTE);
        accreditation.setIdPuntoVenta(ID_PUNTO_VENTA);
        accreditation.setNombrePuntoVenta(PUNTO_VENTA_1);
        accreditation.setFechaRecepcion(LocalDate.now());

        when(accreditationRepository.save(any(Accreditation.class))).thenReturn(accreditation);
        Accreditation result = acreditacionServiceImpl.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA);

        assertNotNull(result);
        assertEquals(IMPORTE, result.getImporte());
        assertEquals(ID_PUNTO_VENTA, result.getIdPuntoVenta());
        assertEquals(PUNTO_VENTA_1, result.getNombrePuntoVenta());
        assertEquals(LocalDate.now(), result.getFechaRecepcion());
        verify(accreditationRepository, times(1)).save(any(Accreditation.class));
    }

    /**
     * Verifies that {@code recibirAcreditacion} throws a {@link PointSaleNotFoundException}
     * when the sales point does not exist.
     */
    @Test
    void recibirAcreditacion_ThrowsIllegalArgumentException() {
        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(List.of());

        Exception exception = assertThrows(PointSaleNotFoundException.class,
                () -> acreditacionServiceImpl.recibirAcreditacion(IMPORTE, INVALID_ID));
        assertEquals(PUNTO_VENTA_NOT_FOUND + ": 99", exception.getMessage());
        verify(accreditationRepository, never()).save(any(Accreditation.class));
    }

    /**
     * Verifies that {@code obtenerAcreditaciones} returns all accreditations
     * from the repository.
     */
    @Test
    void obtenerAcreditaciones_ReturnsAllAcreditaciones() {
        Accreditation accreditation1 = new Accreditation();
        accreditation1.setId(ID_PUNTO_VENTA);
        accreditation1.setImporte(IMPORTE);

        Accreditation accreditation2 = new Accreditation();
        accreditation2.setId(ID_PUNTO_VENTA2);
        accreditation2.setImporte(IMPORTE2);

        when(accreditationRepository.findAll()).thenReturn(Arrays.asList(accreditation1, accreditation2));
        Iterable<Accreditation> result = acreditacionServiceImpl.obtenerAcreditaciones();

        assertNotNull(result);
        assertTrue(((Iterable<?>) result).iterator().hasNext());
        verify(accreditationRepository, times(1)).findAll();
    }
}