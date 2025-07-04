package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import entities.hechos.Hecho;
import org.springframework.http.ResponseEntity;
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
    public void createColeccion(@RequestBody ColeccionInputDTO coleccion){
        this.coleccionService.createColeccion(coleccion);
    }

    @GetMapping("/{idColeccion}")
    public ResponseEntity<List<Hecho>> getColeccion(
            @PathVariable(name = "idColeccion") Long idColeccion,
            @RequestParam(name = "modoNavegacion", defaultValue = "IRRESTRICTO") String modoNavegacion) {
        List<Hecho> hechos = this.coleccionService.getColeccion(idColeccion, modoNavegacion);
        return ResponseEntity.ok(hechos);
    }

    @GetMapping("/cron")
    public ResponseEntity<Void> consensuarHechos() {
        this.coleccionService.consensuarHechos();
        return ResponseEntity.ok().build();
    }
}
