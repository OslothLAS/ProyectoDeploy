package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import entities.colecciones.Handle;
import entities.hechos.Hecho;
import org.springframework.web.bind.annotation.*;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;

import java.util.List;

@RestController
@RequestMapping("/metamapa")
public class MetamapaController {

    private final IMetamapaService MetemapaService;

    public MetamapaController(IMetamapaService MetemapaService) {
        this.MetemapaService = MetemapaService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos() {
        return MetemapaService.getHechos();
    }

    @GetMapping("/{metamapa}/coleccion/{id}")
    public List<Hecho> obtenerHechosXcoleccionXmetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccionX) {

        Handle id_coleccion = new Handle(id_coleccionX);
        return MetemapaService.getHechosXcoleccionXmetamapa(id_coleccion, metamapa);
    }

    @GetMapping("/hechos/categoria")
    public List<Hecho> obtenerHechosXcategoria(@RequestParam("categoria") String categoria) {
        return MetemapaService.getHechosXcategoria(categoria);
    }

    @GetMapping("/hechos/{id}/metamapa")
    public List<Hecho> obtenerHechosXMetamapa(@PathVariable("id") Long id) {
        return MetemapaService.getHechosXmetamapa(id);
    }

}




