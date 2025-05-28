package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import org.springframework.web.bind.annotation.*;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {
    private final IColeccionService coleccionService;
    public ColeccionController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }
    @PostMapping
    public List<Hecho> createColeccion(@RequestBody Coleccion coleccion){
        return this.coleccionService.createColeccion(coleccion);
    }

    @GetMapping("/{idColeccion}")
    public List<Hecho> getColeccion(@PathVariable String idColeccion) {
        return this.coleccionService.getColeccion(idColeccion);
    }
}
