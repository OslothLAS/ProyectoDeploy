package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatController {
    private final IColeccionService coleccionService;
    private final ISolicitudEliminacionService solcitudService;

    public StatController(IColeccionService coleccionService,ISolicitudEliminacionService solcitudService){
        this.coleccionService = coleccionService;
        this.solcitudService = solcitudService;
    }

    @GetMapping("/colecciones/{idColeccion}/provincias-top")
    public ResponseEntity<List<StatDTO>> getHechosDeColeccion(@PathVariable(name = "idColeccion") Long idColeccion) {
        List<StatDTO> stats = this.coleccionService.getProvinciaMasReportada(idColeccion);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias-top")
    public ResponseEntity<List<StatDTO>> getCategoriaPorHechos() {
        List<StatDTO> stats = this.coleccionService.getCategoriaMasReportada();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias/{idCategoria}/provincias-top")
    public ResponseEntity<List<StatDTO>> getProviniciaCategoriaReportada(@PathVariable(name = "idCategoria") Long idCategoria){
        List<StatDTO> stats = this.coleccionService.getProviniciaMasReportadaPorCategoria(idCategoria);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias/{idCategoria}/horas-top")
    public ResponseEntity<List<StatDTO>> getHoraMasReportada(@PathVariable(name = "idCategoria") Long idCategoria) {
        List<StatDTO> stats = this.coleccionService.getHoraMasReportada(idCategoria);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/solicitudes/spam")
    public ResponseEntity<StatDTO> getSpam() {
        return ResponseEntity.ok(this.solcitudService.getCantidadSpam());
    }
}
