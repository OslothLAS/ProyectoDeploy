package ar.utn.frba.ddsi.controllers;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticaController {
    private IEstadisticaService estadisticaService;

    @GetMapping("/{idColeccion}")
    public List<Estadistica> calcularProvinciaPorHechos(@PathVariable(name = "idColeccion") Long idColeccion){
        return estadisticaService.calcularProvinciaPorHechos(idColeccion);
    }

    @GetMapping
    public List<Estadistica> calcularCategoriaPorHechos(){
        return estadisticaService.calcularCategoriaPorHechos();
    }

    @GetMapping("/{idCategoria}")
    public List<Estadistica> calcularProvinciaMasReportada(@PathVariable(name = "idCategoria") Long idCategoria){
        return this.estadisticaService.calcularMaxHechos(idCategoria);
    }

    @GetMapping
    public List<Estadistica> calcularHoraPico(@PathVariable(name = "idCategoria") Long idCategoria){
        return this.estadisticaService.calcularHoraPico(idCategoria);
    }

    @GetMapping
    public List<Estadistica> calcularSolicitudesPorSpam(){
        return this.estadisticaService.calcularSolicitudesPorSpam();
    }
}
