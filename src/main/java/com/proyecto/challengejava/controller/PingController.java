package com.proyecto.challengejava.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Test class to confirm Podman is running correctly in the browser.
 */
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}