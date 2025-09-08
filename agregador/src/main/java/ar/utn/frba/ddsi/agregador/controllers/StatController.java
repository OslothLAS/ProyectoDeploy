package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatController {
    private final IColeccionService coleccionService;

    public StatController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/{idColeccion}/hechos-provincia")
    public ResponseEntity<List<StatDTO>> getHechosDeColeccion(@PathVariable(name = "idColeccion") Long idColeccion) {
        List<StatDTO> stats = this.coleccionService.getHechosProvincia(idColeccion);
        return ResponseEntity.ok(stats);
    }
}
