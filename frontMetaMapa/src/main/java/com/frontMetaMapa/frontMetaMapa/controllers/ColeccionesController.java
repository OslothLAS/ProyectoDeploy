package com.frontMetaMapa.frontMetaMapa.controllers;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.HechoOutputDTO;
import com.frontMetaMapa.frontMetaMapa.services.ColeccionService;
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
public class ColeccionesController {
    private final ColeccionService coleccionService;

    @GetMapping("/buscador-colecciones")
    public String buscadorColecciones(Model model) {
        List<ColeccionOutputDTO> colecciones = coleccionService.obtenerTodasLasColecciones();
        System.out.println("🔍 Colecciones encontradas: " + colecciones);
        model.addAttribute("colecciones", colecciones);
        return "commons/buscadorColecciones";
    }
    @GetMapping("/colecciones/show")
    public String showColeccion(@RequestParam Long id, Model model) {
        ColeccionOutputDTO coleccion = coleccionService.obtenerColeccionPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con id " + id));

        List<HechoOutputDTO> hechos = coleccionService.obtenerHechosPorColeccionId(id);

        model.addAttribute("idColeccion", id);
        model.addAttribute("tituloColeccion", coleccion.getTitulo());
        model.addAttribute("descripcionColeccion", coleccion.getDescripcion());
        model.addAttribute("hechos", hechos);

        return "commons/showColeccion";
    }






}
