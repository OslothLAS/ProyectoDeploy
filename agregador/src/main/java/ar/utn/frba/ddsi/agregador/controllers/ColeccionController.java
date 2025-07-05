package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import entities.colecciones.Fuente;
import entities.colecciones.consenso.strategies.TipoConsenso;
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

    @DeleteMapping("/{idColeccion}")
    public void deleteColeccion(@PathVariable("idColeccion") Long idColeccion) {
        this.coleccionService.deleteColeccion(idColeccion);
    }

    @PostMapping("/{idColeccion}/cambiarConsenso")
    public void cambiarConsenso(@PathVariable("idColeccion") Long idColeccion, @RequestBody TipoConsenso tipo) {
        this.coleccionService.cambiarConsenso(idColeccion, tipo);
    }

    @PostMapping ("/{idColeccion}/fuentes/")
    public void agregarFuente(@PathVariable("idColeccion") Long idColeccion, @RequestBody FuenteInputDTO fuente) {
        this.coleccionService.agregarFuente(idColeccion, fuente);
    }

    @DeleteMapping ("/{idColeccion}/fuentes/{idFuente}")
    public void deleteFuente(@PathVariable("idColeccion") Long idColeccion, @PathVariable("idFuente") Long idFuente) {
        this.coleccionService.eliminarFuente(idColeccion, idFuente);
    }

    @GetMapping("/cron")
    public ResponseEntity<Void> consensuarHechos() {
        this.coleccionService.consensuarHechos();
        return ResponseEntity.ok().build();
    }
}
