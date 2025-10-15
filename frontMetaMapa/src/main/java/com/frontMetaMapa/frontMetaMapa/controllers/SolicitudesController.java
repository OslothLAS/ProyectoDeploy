package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.HechoService;
import com.frontMetaMapa.frontMetaMapa.services.SolicitudEliminacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SolicitudesController {
    private final SolicitudEliminacionService solicitudEliminacionService;
    private final HechoService hechoService;
    @GetMapping("/solicitar-eliminacion")
    public String solicitarEliminacion(@RequestParam Long id, Model model) {
        System.out.println("🔍 el hecho es: " + id);
        HechoOutputDTO hecho = hechoService.obtenerHechoPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrada con id " + id));
        System.out.println("🔍 el hecho es: " + hecho.getTitulo());
        model.addAttribute("idColeccion", id);
        model.addAttribute("tituloHecho", hecho.getTitulo());
        model.addAttribute("descripcionHecho", hecho.getDescripcion());

        return "contribuyente/solicitudEliminacion";
    }
}
