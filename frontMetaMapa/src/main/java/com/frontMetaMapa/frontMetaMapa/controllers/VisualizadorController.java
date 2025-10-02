package com.frontMetaMapa.frontMetaMapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisualizadorController {
    @GetMapping("/visualizador")
    public String visualizador() {
        return "visualizador/index";
    }

    @GetMapping("/buscador-hechos")
    public String buscadorHechos() {
        return "commons/buscadorHechos";
    }

    @GetMapping("/buscador-colecciones")
    public String buscadorColecciones() {
        return "commons/buscadorColecciones";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
