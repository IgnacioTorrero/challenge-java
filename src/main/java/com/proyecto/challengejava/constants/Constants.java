package com.proyecto.challengejava.constants;

import java.util.List;

/* Class used to store all constants */
public class Constants {

    public static final List<String> PUNTOS_VENTA = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String REGEX = "-";
    public static final String UNKNOWN = "Desconocido";
    public static final String AUTORIZATION = "Authorization";
    public static final String OPTIONS = "OPTIONS";
    public static final String BEARER = "Bearer ";

    /* Assemblers */
    public static final String VER_TODOS_PUNTOS_DE_VENTA = "ver-todos-los-puntos-venta";
    public static final String VER_TODAS_ACREDITACIONES = "ver-todas-las-acreditaciones";
    public static final String VER_COSTOS_DESDE_PUNTO = "ver-costos-desde-este-punto";
    public static final String CALCULAR_RUTA_MINIMA = "calcular-ruta-minima";
    public static final String LISTAR_TODOS = "listar-todos";
    public static final String ELIMINAR = "eliminar";
    public static final String RECALCULAR_RUTA = "recalcular-ruta";
    public static final String VER_COSTOS_DESDE = "ver-costos-desde-";

    /* Error messages */
    public static final String PUNTO_VENTA_NOT_FOUND = "Punto/s de venta no encontrado/s";
    public static final String COSTO_PUNTOS_LESS_THAN_ZERO = "El costo no puede ser menor a cero";
    public static final String INVALID_ID_EXCEPTION = "Ambos IDs no pueden ser iguales";
    public static final String PUNTO_VENTA_ALREADY_EXISTS = "El punto de venta ya existe";
    public static final String FORMATO_INVALIDO = "Formato inválido en la solicitud. Verifica los valores enviados.";
    public static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado con email: ";
}