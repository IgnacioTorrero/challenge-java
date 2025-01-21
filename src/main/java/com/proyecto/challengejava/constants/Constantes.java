package com.proyecto.challengejava.constants;

import java.util.List;

/* Clase utilizada para almacenar todas las constantes */
public class Constantes {

    public static final List<String> PUNTOS_VENTA = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String REGEX = "-";
    public static final String UNKNOWN = "Desconocido";

    /* Mensajes de error */
    public static final String PUNTO_VENTA_NOT_FOUND = "Punto/s de venta no encontrado/s";
    public static final String COSTO_PUNTOS_LESS_THAN_ZERO = "El costo no puede ser menor a cero";
}