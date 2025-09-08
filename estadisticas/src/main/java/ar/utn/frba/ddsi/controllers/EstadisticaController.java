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
    public List<Estadistica> calcularHechosPorProvincia(@PathVariable(name = "idColeccion") Long idColeccion){
        return estadisticaService.calcularHechosPorProvincia(idColeccion);
    }
}
