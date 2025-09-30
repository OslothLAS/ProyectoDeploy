package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies.TipoConsenso;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.List;

import static utils.HechoUtil.hechosToDTO;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {
    private final IColeccionService coleccionService;
    public ColeccionController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public ResponseEntity<List<Coleccion>> getColecciones(){
        return ResponseEntity.ok(this.coleccionService.getColecciones());
    }

    @PostMapping
    public void createColeccion(@RequestBody ColeccionInputDTO coleccion){
        this.coleccionService.createColeccion(coleccion);
    }

    @GetMapping("/{idColeccion}/hechos")
    public ResponseEntity<List<HechoOutputDTO>> getHechosDeColeccion(
            @PathVariable(name = "idColeccion") Long idColeccion,
            @RequestParam(name = "modoNavegacion", defaultValue = "IRRESTRICTO") String modoNavegacion) {
        List<Hecho> hechos = this.coleccionService.getHechosDeColeccion(idColeccion, modoNavegacion);
        return ResponseEntity.ok(hechosToDTO(hechos));
    }

    @DeleteMapping("/{idColeccion}")
    public ResponseEntity<Void> deleteColeccion(@PathVariable("idColeccion") Long idColeccion) {
        this.coleccionService.deleteColeccion(idColeccion);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idColeccion}/cambiarConsenso")
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

    @GetMapping("/cronConsensuar")
    public ResponseEntity<Void> consensuarHechos() {
        this.coleccionService.consensuarHechos();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cronActualizar")
    public ResponseEntity<Void> actualizarHechos(){
        this.coleccionService.actualizarHechos();
        return ResponseEntity.ok().build();
    }
}
