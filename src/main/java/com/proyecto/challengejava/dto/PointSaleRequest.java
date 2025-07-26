package com.proyecto.challengejava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PointSaleRequest {

    @NotBlank(message = "The name cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9_ ]+$", message = "The name cannot contain letters, numbers, spaces, and underscores")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
