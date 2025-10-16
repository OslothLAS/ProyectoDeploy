package com.frontMetaMapa.frontMetaMapa.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ContribuyenteController {

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
    public String contribuyente() {
        return "contribuyente/index";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/subir-hecho")
    public String subirHecho() {
        return "contribuyente/subirHecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @GetMapping("/solicitud-eliminacion")
    public String solicitudEliminacion() {
        return "contribuyente/solicitudEliminacion";
    }
}
