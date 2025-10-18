package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.SolicitudApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.SolicitudOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.SolicitudEliminacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class AdministradorController {

    private final ColeccionService coleccionService;
    private final SolicitudEliminacionService solicitudEliminacionService;
    private final HechoService hechoService;

    @ModelAttribute
    public void addRolToModel(Model model, Authentication authentication) {
        if (authentication != null) {
            String rol = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("SIN_ROL");
            model.addAttribute("rol", rol);
        } else {
            model.addAttribute("rol", "ANONIMO");
        }
    }

    @GetMapping("/administrador")
    public String administrador(Model model) {
        List<HechoApiOutputDto> hechos = hechoService.obtenerTodosLosHechos();

        // Tomar solo los primeros 3
        List<HechoApiOutputDto> primerosTres = hechos.stream()
                .limit(3)
                .toList(); // .collect(Collectors.toList()) si usás Java < 16

        model.addAttribute("hechos", primerosTres);
        return "administrador/index";
    }

    @GetMapping("/dashboard-solicitudes")
    public String solicitudes(Model model) {
        List<SolicitudOutputDTO> solicitudes = solicitudEliminacionService.obtenerSolicitudes();
        model.addAttribute("solicitudes", solicitudes);
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
