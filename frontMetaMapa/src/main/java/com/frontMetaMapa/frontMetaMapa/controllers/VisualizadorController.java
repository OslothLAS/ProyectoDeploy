package com.frontMetaMapa.frontMetaMapa.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class VisualizadorController {
    @GetMapping("/visualizador")
    public String visualizador() {
        return "visualizador/index";
    }
}
