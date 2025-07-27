package com.proyecto.challengejava.constants;

import java.util.List;

/* Class used to store all constants */
public class ConstantsTest {

    public static final List<String> POINTS_OF_SALE = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String POINT_SALE_1 = "CABA";
    public static final String POINT_SALE_2 = "GBA_1";
    public static final String POINT_SALE_3 = "La Rioja";
    public static final String POINT_SALE_4 = "GBA_2";
    public static final String POINT_SALE_5 = "Gran Buenos Aires 1";
    public static final String POINT_SALE_6 = "Uruguay";
    public static final Long INVALID_ID = 99L;
    public static final Long INVALID_ID2 = 100L;
    public static final double INVALID_COST = -10.0;
    public static final int SUCCESS_RESPONSE = 200;
    public static final Long ID_POINT_SALE1 = 1L;
    public static final Long ID_POINT_SALE2 = 2L;
    public static final Long ID_POINT_SALE3 = 3L;
    public static final Long ID_POINT_SALE4 = 10L;
    public static final Long ID_POINT_SALE5 = 11L;
    public static final double AMOUNT = 100.0;
    public static final double AMOUNT2 = 200.0;
    public static final double AMOUNT3 = 150.0;
    public static final int ZERO = 0;
    public static final String SEE_COSTS_FROM_1 = "ver-costos-desde-1";
    public static final String SEE_COSTS_FROM_2 = "ver-costos-desde-2";
    public static final String SEE_COSTS_FROM_3 = "ver-costos-desde-3";
    public static final String RECALCULATE_ROUTE = "recalcular-ruta";

    /* Methods */
    public static final String METHOD_PRELOAD_CACHE = "preloadCache";

    /* Fields */
    public static final String FIELD_CACHE = "cache";

    /* Error messages */
    public static final String POINT_OF_SALE_NOT_FOUND = "Point of sale/s not found";
    public static final String COST_POINTS_LESS_THAN_ZERO = "The cost cannot be less than zero";
    public static final String INVALID_ID_EXCEPTION = "Both IDs cannot be equal";
    public static final String MISSING_COST_BETWEEN = "Missing cost between 2 and 3";
}