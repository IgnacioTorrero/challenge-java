package com.proyecto.challengejava.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Clase de prueba para confirmar el funcionamiento del Podman en el navegador.
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
