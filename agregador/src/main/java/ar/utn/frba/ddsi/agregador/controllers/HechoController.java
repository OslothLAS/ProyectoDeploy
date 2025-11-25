package ar.utn.frba.ddsi.agregador.controllers;

import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hechos")
public class HechoController {
    private final IColeccionService coleccionService;

    public HechoController(IColeccionService coleccionService){
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public List<HechoOutputDTO> obtenerTodosLosHechos(@RequestParam Map<String, String> filtros){
        return this.coleccionService.obtenerTodosLosHechos(filtros);
    }

    @GetMapping("/{id}")
    public HechoOutputDTO obtenerHechoPorId(@PathVariable("id") Long id){
        return this.coleccionService.obtenerHechoPorId(id);
    }

    @GetMapping("/fuentes")
    public List<Fuente> obtenerFuentes(){
        return this.coleccionService.getFuentes();
    }
}
