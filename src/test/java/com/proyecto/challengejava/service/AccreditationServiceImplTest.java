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
     * Verifies that {@code receiveAccreditation} correctly saves and returns an accreditation
     * when the sales point exists.
     */
    @Test
    void receiveAccreditation() {
        PointSale pointSale = new PointSale();
        pointSale.setId(ID_POINT_SALE1);
        pointSale.setName(POINT_SALE_1);

        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(List.of(pointSale));

        Accreditation accreditation = new Accreditation();
        accreditation.setId(ID_POINT_SALE1);
        accreditation.setAmount(AMOUNT);
        accreditation.setIdPointSale(ID_POINT_SALE1);
        accreditation.setPointSaleName(POINT_SALE_1);
        accreditation.setDateReception(LocalDate.now());

        when(accreditationRepository.save(any(Accreditation.class))).thenReturn(accreditation);
        Accreditation result = acreditacionServiceImpl.receiveAccreditation(AMOUNT, ID_POINT_SALE1);

        assertNotNull(result);
        assertEquals(AMOUNT, result.getAmount());
        assertEquals(ID_POINT_SALE1, result.getIdPointSale());
        assertEquals(POINT_SALE_1, result.getPointSaleName());
        assertEquals(LocalDate.now(), result.getDateReception());
        verify(accreditationRepository, times(1)).save(any(Accreditation.class));
    }

    /**
     * Verifies that {@code receiveAccreditation} throws a {@link PointSaleNotFoundException}
     * when the sales point does not exist.
     */
    @Test
    void receiveAccreditation_ThrowsIllegalArgumentException() {
        when(puntoVentaServiceImpl.getAllPointSale()).thenReturn(List.of());

        Exception exception = assertThrows(PointSaleNotFoundException.class,
                () -> acreditacionServiceImpl.receiveAccreditation(AMOUNT, INVALID_ID));
        assertEquals(POINT_SALE_NOT_FOUND + ": 99", exception.getMessage());
        verify(accreditationRepository, never()).save(any(Accreditation.class));
    }

    /**
     * Verifies that {@code getAccreditations} returns all accreditations
     * from the repository.
     */
    @Test
    void getAccreditations() {
        Accreditation accreditation1 = new Accreditation();
        accreditation1.setId(ID_POINT_SALE1);
        accreditation1.setAmount(AMOUNT);

        Accreditation accreditation2 = new Accreditation();
        accreditation2.setId(ID_POINT_SALE2);
        accreditation2.setAmount(AMOUNT2);

        when(accreditationRepository.findAll()).thenReturn(Arrays.asList(accreditation1, accreditation2));
        Iterable<Accreditation> result = acreditacionServiceImpl.getAccreditations();

        assertNotNull(result);
        assertTrue(((Iterable<?>) result).iterator().hasNext());
        verify(accreditationRepository, times(1)).findAll();
    }
}