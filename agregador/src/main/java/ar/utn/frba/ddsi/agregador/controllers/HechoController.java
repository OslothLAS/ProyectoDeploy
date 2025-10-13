package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    private final IColeccionService coleccionService;

    public HechoController(IColeccionService coleccionService){
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public List<HechoOutputDTO> obtenerTodosLosHechos(){
        return this.coleccionService.obtenerTodosLosHechos();
    }
}
