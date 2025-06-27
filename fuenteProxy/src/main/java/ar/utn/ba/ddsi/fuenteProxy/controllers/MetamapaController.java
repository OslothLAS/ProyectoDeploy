package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
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

    @GetMapping("/coleccion/{id}")
    public List<Hecho> obtenerHechosXcoleccion(@PathVariable("id") Long id) {
        return MetemapaService.getHechosXcoleccion(id);
    }

    @GetMapping("/hechos/categoria")
    public List<Hecho> obtenerHechosXcategoria(@RequestParam("categoria") String categoria) {
        return MetemapaService.getHechosXcategoria(categoria);
    }

    @GetMapping("/hechos/metamapa/{id}")
    public List<Hecho> obtenerHechosXMetamapa(@PathVariable("id") Long id) {
        return MetemapaService.getHechosXmetamapa(id);
    }

}




