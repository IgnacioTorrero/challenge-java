package com.proyecto.challengejava.constants;

import java.util.List;

/* Clase utilizada para almacenar todas las constantes de los test */
public class ConstantesTest {

    public static final List<String> PUNTOS_VENTA = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String PUNTO_VENTA_1 = "CABA";
    public static final String PUNTO_VENTA_2 = "GBA_1";
    public static final String PUNTO_VENTA_3 = "La Rioja";
    public static final String PUNTO_VENTA_4 = "Capital Federal";
    public static final String PUNTO_VENTA_5 = "Gran Buenos Aires 1";
    public static final String PUNTO_VENTA_6 = "Uruguay";
    public static final String METHOD_AGREGAR_COSTO_INICIAL = "agregarCostoInicial";
    public static final Long INVALID_ID = 99L;
    public static final Long INVALID_ID2 = 100L;
    public static final double INVALID_COSTO = -10.0;
    public static final int SUCCESS_RESPONSE = 200;
    public static final Long ID_PUNTO_VENTA = 1L;
    public static final Long ID_PUNTO_VENTA2 = 2L;
    public static final Long ID_PUNTO_VENTA3 = 3L;
    public static final Long ID_PUNTO_VENTA4 = 10L;
    public static final Long ID_PUNTO_VENTA5 = 11L;
    public static final double IMPORTE = 100.0;
    public static final double IMPORTE2 = 200.0;

    /* Mensajes de error */
    public static final String PUNTO_VENTA_NOT_FOUND = "Punto/s de venta no encontrado/s";
    public static final String COSTO_PUNTOS_LESS_THAN_ZERO = "El costo no puede ser menor a cero";
}