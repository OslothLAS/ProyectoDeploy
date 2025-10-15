package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.HechoInputDTO;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HechosController {
    private final HechoService hechoService;

    @GetMapping("/buscador-hechos")
    public String buscadorHechos() {
        return "commons/buscadorHechos";
    }

    @GetMapping("/hecho/{id}")
    public String detalleHecho(@PathVariable Long id, Model model) {
        model.addAttribute("idHecho", id);
        return "commons/detalleHecho";
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/hecho/{id}/editar")
    public String editarHecho(@PathVariable Long id, Model model) {
        model.addAttribute("idHecho", id);
        return "commons/editarHecho";
    }

    @PreAuthorize("hasAnyRole('CONTRIBUYENTE')")
    @PostMapping("/hecho")
    public String crearHecho(@ModelAttribute HechoInputDTO hechoInputDTO) {
        hechoService.crearHecho(hechoInputDTO);
        return "commons/buscadorHechos"; // o redirección al mensaje de éxito
    }
}
