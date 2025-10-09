package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.input.FuenteInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.consenso.strategies.TipoConsenso;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;

import java.util.List;

import static ar.utn.frba.ddsi.agregador.utils.HechoUtil.hechosToDTO;
import static ar.utn.frba.ddsi.agregador.utils.ColeccionUtil.dtoToColeccion;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {
    private final IColeccionService coleccionService;
    public ColeccionController(IColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public ResponseEntity<List<ColeccionOutputDTO>> getColecciones(){
        return ResponseEntity.ok(this.coleccionService.getColecciones());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createColeccion(@RequestBody ColeccionInputDTO coleccion){
        this.coleccionService.createColeccion(coleccion);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idColeccion}/hechos")
    public ResponseEntity<List<HechoOutputDTO>> getHechosDeColeccion(
            @PathVariable(name = "idColeccion") Long idColeccion,
            @RequestParam(name = "modoNavegacion", defaultValue = "IRRESTRICTO") String modoNavegacion) {
        List<Hecho> hechos = this.coleccionService.getHechosDeColeccion(idColeccion, modoNavegacion);
        return ResponseEntity.ok(hechosToDTO(hechos));
    }

    @DeleteMapping("/{idColeccion}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteColeccion(@PathVariable("idColeccion") Long idColeccion) {
        this.coleccionService.deleteColeccion(idColeccion);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idColeccion}/cambiarConsenso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarConsenso(@PathVariable("idColeccion") Long idColeccion, @RequestBody TipoConsenso tipo) {
        this.coleccionService.cambiarConsenso(idColeccion, tipo);
        return ResponseEntity.ok().build();
    }

    @PostMapping ("/{idColeccion}/fuentes/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> agregarFuente(@PathVariable("idColeccion") Long idColeccion, @RequestBody FuenteInputDTO fuente) {
        this.coleccionService.agregarFuente(idColeccion, fuente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping ("/{idColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFuente(@PathVariable("idColeccion") Long idColeccion, @PathVariable("idFuente") Long idFuente) {
        this.coleccionService.eliminarFuente(idColeccion, idFuente);
        return ResponseEntity.ok().build();
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
