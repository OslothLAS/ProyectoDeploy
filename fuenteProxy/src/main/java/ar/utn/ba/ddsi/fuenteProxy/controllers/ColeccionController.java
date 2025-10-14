package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.services.IColeccionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/metamapa/colecciones")
public class ColeccionController {

    private final IColeccionService ColeccionService;

    public ColeccionController(IColeccionService ColeccionService) {
        this.ColeccionService = ColeccionService;
    }

    @GetMapping("/{metamapa}")
    public List<ColeccionDto> obtenerColeccionesXMetamapa(
            @PathVariable("metamapa") Long metamapa){
        return ColeccionService.getColeccionesXmetamapa(metamapa);
    }
}
