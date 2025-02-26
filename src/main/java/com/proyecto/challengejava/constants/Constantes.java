package com.proyecto.challengejava.constants;

import java.util.List;

/* Clase utilizada para almacenar todas las constantes */
public class Constantes {

    public static final List<String> PUNTOS_VENTA = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String REGEX = "-";
    public static final String REGEX_2 = "^[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+$";
    public static final String UNKNOWN = "Desconocido";

    /* Mensajes de error */
    public static final String PUNTO_VENTA_NOT_FOUND = "Punto/s de venta no encontrado/s";
    public static final String COSTO_PUNTOS_LESS_THAN_ZERO = "El costo no puede ser menor a cero";
    public static final String RUTA_NOT_FOUND_EXCEPTION = "No existe un camino válido entre los puntos ingresados";
    public static final String INVALID_ID_EXCEPTION = "Los IDs de los puntos de venta deben ser positivos";
    public static final String INVALID_ID_EXCEPTION_2 = "No se puede agregar el mismo ID como punto de venta";
    public static final String INVALID_ID_EXCEPTION_3 = "Los IDs de los puntos de venta deben ser validos";
    public static final String INVALID_NAME_EXCEPTION = "El nombre del punto de venta debe ser válido";
    public static final String IMPORTE_LESS_THAN_ZERO = "El importe no puede ser menor a cero";
    public static final String INTERNAL_SERVER_ERROR = "Ocurrió un error inesperado";
}