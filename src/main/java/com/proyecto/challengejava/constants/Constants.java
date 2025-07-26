package com.proyecto.challengejava.constants;

import java.util.List;

/* Class used to store all constants */
public class Constants {

    public static final List<String> POINTS_OF_SALE = List.of("CABA", "GBA_1", "GBA_2", "Santa Fe", "Córdoba", "Misiones", "Salta",
            "Chubut", "Santa Cruz", "Catamarca");
    public static final String REGEX = "-";
    public static final String UNKNOWN = "unknown";
    public static final String AUTHORIZATION = "Authorization";
    public static final String OPTIONS = "OPTIONS";
    public static final String BEARER = "Bearer ";

    /* Assemblers */
    public static final String SEE_ALL_POINTS_OF_SALE = "see-all-points-of-sale";
    public static final String SEE_ALL_ACCREDITATIONS = "see-all-accreditations";
    public static final String SEE_COSTS_FROM_POINT = "see-costs-from-this-point";
    public static final String CALCULATE_MIN_ROUTE = "calculate-min-route";
    public static final String LIST_ALL = "list-all";
    public static final String DELETE = "delete";
    public static final String RECALCULATE_ROUTE = "recalculate-route";
    public static final String SEE_COSTS_FROM = "see-costs-from-";

    /* Error messages */
    public static final String POINT_OF_SALE_NOT_FOUND = "Point of sale/s not found";
    public static final String COST_POINTS_LESS_THAN_ZERO = "The cost cannot be less than zero";
    public static final String INVALID_ID_EXCEPTION = "Both IDs cannot be equal";
    public static final String POINT_OF_SALE_ALREADY_EXISTS = "The point of sale already exists";
    public static final String INVALID_FORMAT = "Invalid format in the request. Please check the submitted values.";
    public static final String USER_NOT_FOUND = "Username not found with email: ";
}