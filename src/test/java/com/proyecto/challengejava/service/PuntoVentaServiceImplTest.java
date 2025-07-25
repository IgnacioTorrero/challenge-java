package com.proyecto.challengejava.service;

import com.proyecto.challengejava.entity.PuntoVenta;
import com.proyecto.challengejava.repository.PuntoVentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_ALREADY_EXISTS;
import static com.proyecto.challengejava.constants.ConstantesTest.*;
import static com.proyecto.challengejava.constants.Constantes.PUNTO_VENTA_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PuntoVentaServiceImplTest {

    @Mock
    private PuntoVentaRepository puntoVentaRepository;

    @InjectMocks
    private PuntoVentaServiceImpl service;

    private List<PuntoVenta> datosMock;

    @BeforeEach
    void setUp() {
        datosMock = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            datosMock.add(new PuntoVenta(i, "Punto " + i));
        }

        when(puntoVentaRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(datosMock));

        when(puntoVentaRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return datosMock.stream().filter(p -> p.getId().equals(id)).findFirst();
        });

        when(puntoVentaRepository.existsById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return datosMock.stream().anyMatch(p -> p.getId().equals(id));
        });

        when(puntoVentaRepository.save(any(PuntoVenta.class))).thenAnswer(invocation -> {
            PuntoVenta nuevo = invocation.getArgument(0);
            if (nuevo.getId() == null) {
                nuevo.setId((long) (datosMock.size() + 1));
            } else {
                datosMock.removeIf(p -> p.getId().equals(nuevo.getId()));
            }
            datosMock.add(nuevo);
            return nuevo;
        });

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            datosMock.removeIf(p -> p.getId().equals(id));
            return null;
        }).when(puntoVentaRepository).deleteById(anyLong());
    }

    @Test
    void precargarCache_CargaDatosEnElCache() throws Exception {
        var method = PuntoVentaServiceImpl.class.getDeclaredMethod("precargarCache");
        method.setAccessible(true);
        method.invoke(service);

        var field = PuntoVentaServiceImpl.class.getDeclaredField("cache");
        field.setAccessible(true);
        Map<Long, String> cache = (Map<Long, String>) field.get(service);

        assertEquals(10, cache.size());
        assertEquals("Punto 1", cache.get(1L));
        assertEquals("Punto 10", cache.get(10L));
    }

    @Test
    void getAllPuntosVenta_ReturnListOfPuntosVenta() {
        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();

        assertNotNull(puntosVenta);
        assertEquals(10, puntosVenta.size());
        assertEquals(1L, puntosVenta.get(0).getId());
        assertEquals("Punto 1", puntosVenta.get(0).getNombre());
    }

    @Test
    void addPuntoVenta_AddsNewPuntoVenta() {
        service.addPuntoVenta(PUNTO_VENTA_3);

        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();
        assertEquals(11, puntosVenta.size());

        PuntoVenta agregado = puntosVenta.stream()
                .filter(p -> PUNTO_VENTA_3.equals(p.getNombre()))
                .findFirst()
                .orElse(null);
        assertNotNull(agregado);
    }

    @Test
    void addPuntoVenta_ThrowsIllegalArgumentException_WhenNombreAlreadyExists() {
        // Arrange
        String nombreDuplicado = "Punto 5";

        when(puntoVentaRepository.existsByNombre(nombreDuplicado)).thenReturn(true);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.addPuntoVenta(nombreDuplicado)
        );

        assertEquals(PUNTO_VENTA_ALREADY_EXISTS, ex.getMessage());
    }

    @Test
    void updatePuntoVenta_UpdatesExistingPuntoVenta() {
        Long idParaActualizar = 2L;
        service.updatePuntoVenta(idParaActualizar, PUNTO_VENTA_5);

        PuntoVenta actualizado = service.getAllPuntosVenta().stream()
                .filter(p -> p.getId().equals(idParaActualizar))
                .findFirst()
                .orElse(null);
        assertNotNull(actualizado);
        assertEquals(PUNTO_VENTA_5, actualizado.getNombre());
    }

    @Test
    void updatePuntoVenta_ThrowsIllegalArgumentException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.updatePuntoVenta(INVALID_ID, PUNTO_VENTA_6));
        assertEquals(PUNTO_VENTA_NOT_FOUND, ex.getMessage());
    }

    @Test
    void deletePuntoVenta_RemovesPuntoVenta() {
        Long idAEliminar = 4L;
        service.deletePuntoVenta(idAEliminar);

        List<PuntoVenta> puntosVenta = service.getAllPuntosVenta();
        assertEquals(9, puntosVenta.size());
        assertTrue(puntosVenta.stream().noneMatch(p -> p.getId().equals(idAEliminar)));
    }

    @Test
    void deletePuntoVenta_ThrowsIllegalArgumentException_WhenIdNotExists() {
        // Arrange
        Long idInexistente = 999L;

        when(puntoVentaRepository.existsById(idInexistente)).thenReturn(false);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                service.deletePuntoVenta(idInexistente)
        );

        assertEquals(PUNTO_VENTA_NOT_FOUND, ex.getMessage());
    }
}
