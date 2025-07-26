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
 * Unit test for {@link AccreditationServiceImpl}.
 * Validates logic related to accreditation creation and retrieval.
 */
public class AccreditationServiceImplTest {

    @Mock
    private AccreditationRepository accreditationRepository;

    @Mock
    private PointSaleServiceImpl puntoVentaServiceImpl;

    @InjectMocks
    private AccreditationServiceImpl acreditacionServiceImpl;

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
        pointSale.setName(PUNTO_VENTA_1);

        when(puntoVentaServiceImpl.getAllPuntosVenta()).thenReturn(List.of(pointSale));

        Accreditation accreditation = new Accreditation();
        accreditation.setId(ID_PUNTO_VENTA);
        accreditation.setAmount(IMPORTE);
        accreditation.setIdPointSale(ID_PUNTO_VENTA);
        accreditation.setPointSaleName(PUNTO_VENTA_1);
        accreditation.setDateReception(LocalDate.now());

        when(accreditationRepository.save(any(Accreditation.class))).thenReturn(accreditation);
        Accreditation result = acreditacionServiceImpl.recibirAcreditacion(IMPORTE, ID_PUNTO_VENTA);

        assertNotNull(result);
        assertEquals(IMPORTE, result.getAmount());
        assertEquals(ID_PUNTO_VENTA, result.getIdPointSale());
        assertEquals(PUNTO_VENTA_1, result.getPointSaleName());
        assertEquals(LocalDate.now(), result.getDateReception());
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
        accreditation1.setAmount(IMPORTE);

        Accreditation accreditation2 = new Accreditation();
        accreditation2.setId(ID_PUNTO_VENTA2);
        accreditation2.setAmount(IMPORTE2);

        when(accreditationRepository.findAll()).thenReturn(Arrays.asList(accreditation1, accreditation2));
        Iterable<Accreditation> result = acreditacionServiceImpl.obtenerAcreditaciones();

        assertNotNull(result);
        assertTrue(((Iterable<?>) result).iterator().hasNext());
        verify(accreditationRepository, times(1)).findAll();
    }
}