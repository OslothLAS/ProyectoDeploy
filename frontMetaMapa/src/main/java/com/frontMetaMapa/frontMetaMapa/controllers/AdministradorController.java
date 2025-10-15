package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class AdministradorController {
    private final ColeccionService coleccionService;
    @GetMapping("/administrador")
    public String administrador() {
        return "administrador/index";
    }

    @GetMapping("/dashboard-solicitudes")
    public String solicitudes() {
        return "administrador/dashboardSolicitud";
    }

    @GetMapping("/panel-control")
    public String panelControl() {
        return "administrador/panelControl";
    }

    @GetMapping("/mis-colecciones")
    public String misColecciones(Model model, Authentication authentication) {
        List<ColeccionOutputDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        model.addAttribute("titulo", "Listado de Colecciones");
        model.addAttribute("totalDeAlumnos", colecciones.size());
        model.addAttribute("usuario", authentication.getName());
        return "administrador/verColecciones";
    }

    @GetMapping("/subirCsv")
    public String subirCsv() {
        return "administrador/uploadCSV";
    }
}
