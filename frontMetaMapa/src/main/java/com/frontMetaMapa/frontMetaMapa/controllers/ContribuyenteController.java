package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.HechoApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.SolicitudOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.SolicitudEliminacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContribuyenteController {

    private final HechoService hechoService;
    private final SolicitudEliminacionService solicitudEliminacionService;


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

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/contribuyente")
    public String contribuyente(Model model, Authentication authentication) {
        List<HechoApiOutputDto> hechos = hechoService.obtenerTodosLosHechos();

        // Tomar solo los primeros 3
        List<HechoApiOutputDto> primerosTres = hechos.stream()
                .limit(3)
                .toList(); // .collect(Collectors.toList()) si usás Java < 16

        model.addAttribute("hechos", primerosTres);
        model.addAttribute("username",authentication.getName());
        return "contribuyente/index";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/mis-solicitudes")
    public String solicitudes(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<SolicitudOutputDTO> solicitudes = solicitudEliminacionService.obtenerSolicitudesPorUsername(username);
        model.addAttribute("solicitudes", solicitudes);
        return "administrador/dashboardSolicitud";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/subir-hecho")
    public String subirHecho() {
        return "contribuyente/subirHecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/crear-hecho")
    public String mostrarFormularioCreacion(Model model) {
        return "contribuyente/subirHecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/solicitud-eliminacion")
    public String solicitudEliminacion() {
        return "contribuyente/solicitudEliminacion";
    }
}
