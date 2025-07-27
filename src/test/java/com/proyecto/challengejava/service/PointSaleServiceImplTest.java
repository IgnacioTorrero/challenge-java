package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PointSale;
import com.proyecto.challengejava.repository.PointSaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static com.proyecto.challengejava.constants.Constants.POINT_OF_SALE_ALREADY_EXISTS;
import static com.proyecto.challengejava.constants.ConstantsTest.*;
import static com.proyecto.challengejava.constants.Constants.POINT_OF_SALE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link PointSaleServiceImpl}.
 * Validates business logic related to sales point operations: add, update, delete, fetch and cache.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PointSaleServiceImplTest {

    @Mock
    private PointSaleRepository pointSaleRepository;

    @InjectMocks
    private PointSaleServiceImpl service;

    private List<PointSale> datosMock;

    /**
     * Initializes mock repository behavior and sets up an in-memory list to simulate database operations.
     */
    @BeforeEach
    void setUp() {
        datosMock = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            datosMock.add(new PointSale(i, "Point " + i));
        }

        when(pointSaleRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(datosMock));

        when(pointSaleRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return datosMock.stream().filter(p -> p.getId().equals(id)).findFirst();
        });

        when(pointSaleRepository.existsById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return datosMock.stream().anyMatch(p -> p.getId().equals(id));
        });

        when(pointSaleRepository.save(any(PointSale.class))).thenAnswer(invocation -> {
            PointSale newPoint = invocation.getArgument(0);
            if (newPoint.getId() == null) {
                newPoint.setId((long) (datosMock.size() + 1));
            } else {
                datosMock.removeIf(p -> p.getId().equals(newPoint.getId()));
            }
            datosMock.add(newPoint);
            return newPoint;
        });

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            datosMock.removeIf(p -> p.getId().equals(id));
            return null;
        }).when(pointSaleRepository).deleteById(anyLong());
    }

    /**
     * Verifies that {@code preloadCache} correctly loads all points into the internal cache using reflection.
     */
    @Test
    void preloadCache_LoadDataInCache() throws Exception {
        // Act
        var method = PointSaleServiceImpl.class.getDeclaredMethod(METHOD_PRELOAD_CACHE);
        method.setAccessible(true);
        method.invoke(service);

        var field = PointSaleServiceImpl.class.getDeclaredField(FIELD_CACHE);
        field.setAccessible(true);
        Map<Long, String> cache = (Map<Long, String>) field.get(service);

        // Assert
        assertEquals(10, cache.size());
        assertEquals("Point 1", cache.get(ID_POINT_SALE1));
        assertEquals("Point 10", cache.get(ID_POINT_SALE4));
    }

    /**
     * Verifies that {@code getAllSalePoints} returns all existing points of sale.
     */
    @Test
    void getAllSalePoints_ReturnListOfPointSale() {
        // Act
        List<PointSale> salePoints = service.getAllPointSale();

        // Assert
        assertNotNull(salePoints);
        assertEquals(10, salePoints.size());
        assertEquals(1L, salePoints.get(0).getId());
        assertEquals("Point 1", salePoints.get(0).getName());
    }

    /**
     * Verifies that {@code addPointSale} successfully adds a new sales point.
     */
    @Test
    void addPointSale_AddsNewPointSale() {
        // Act
        service.addPointSale(POINT_SALE_3);

        // Assert
        List<PointSale> salePoints = service.getAllPointSale();
        assertEquals(11, salePoints.size());

        PointSale aggregatedPoint = salePoints.stream()
                .filter(p -> POINT_SALE_3.equals(p.getName()))
                .findFirst()
                .orElse(null);
        assertNotNull(aggregatedPoint);
    }

    /**
     * Verifies that {@code addPointSale} throws exception if name already exists.
     */
    @Test
    void addPointSale_ThrowsIllegalArgumentException_WhenNameAlreadyExists() {
        // Arrange
        String duplicatedName = "Point 5";
        when(pointSaleRepository.existsByName(duplicatedName)).thenReturn(true);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.addPointSale(duplicatedName)
        );
        assertEquals(POINT_OF_SALE_ALREADY_EXISTS, ex.getMessage());
    }

    /**
     * Verifies that {@code updatePointSale} updates the name of an existing sales point.
     */
    @Test
    void updatePointSale_UpdatesExistingPointSale() {
        // Act
        Long idForUpdate = 2L;
        service.updatePointSale(idForUpdate, POINT_SALE_5);

        // Assert
        PointSale updated = service.getAllPointSale().stream()
                .filter(p -> p.getId().equals(idForUpdate))
                .findFirst()
                .orElse(null);
        assertNotNull(updated);
        assertEquals(POINT_SALE_5, updated.getName());
    }

    /**
     * Verifies that {@code updatePointSale} throws exception if the sales point does not exist.
     */
    @Test
    void updatePointSale_ThrowsIllegalArgumentException() {
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.updatePointSale(INVALID_ID, POINT_SALE_6));
        assertEquals(POINT_OF_SALE_NOT_FOUND, ex.getMessage());
    }

    /**
     * Verifies that {@code deletePointSale} removes a sales point by ID.
     */
    @Test
    void deletePointSale_RemovesPointSale() {
        // Act
        Long idToDelete = 4L;
        service.deletePointSale(idToDelete);

        // Assert
        List<PointSale> salePoints = service.getAllPointSale();
        assertEquals(9, salePoints.size());
        assertTrue(salePoints.stream().noneMatch(p -> p.getId().equals(idToDelete)));
    }

    /**
     * Verifies that {@code deletePointSale} throws exception if the ID does not exist.
     */
    @Test
    void deletePointSale_ThrowsIllegalArgumentException_WhenIdNotExists() {
        // Arrange
        Long nonExistingId = 999L;
        when(pointSaleRepository.existsById(nonExistingId)).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.deletePointSale(nonExistingId)
        );
        assertEquals(POINT_OF_SALE_NOT_FOUND, ex.getMessage());
    }
}
