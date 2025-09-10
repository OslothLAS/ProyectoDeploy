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

    @GetMapping("/colecciones/provincias-top")
    public ResponseEntity<List<StatDTO>> getHechosDeColeccion() {
        List<StatDTO> stats = this.coleccionService.getProvinciaMasReportadaPorTodasLasColecciones();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias-top")
    public ResponseEntity<List<StatDTO>> getCategoriaPorHechos() {
        List<StatDTO> stats = this.coleccionService.getCategoriaMasReportada();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias/provincias-top")
    public ResponseEntity<List<StatDTO>> getProviniciaCategoriaReportada(){
        List<StatDTO> stats = this.coleccionService.getProviniciaMasReportadaPorCategoria();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/categorias/horas-top")
    public ResponseEntity<List<StatDTO>> getHoraMasReportada() {
        List<StatDTO> stats = this.coleccionService.getHoraMasReportada();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/solicitudes/spam")
    public ResponseEntity<StatDTO> getSpam() {
        return ResponseEntity.ok(this.solcitudService.getCantidadSpam());
    }
}
