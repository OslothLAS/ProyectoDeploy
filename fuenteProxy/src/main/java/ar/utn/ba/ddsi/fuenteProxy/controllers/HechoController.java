package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Handle;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.services.IHechoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/metamapa/hechos")
public class HechoController {
    private final IHechoService HechoService;

    public HechoController(IHechoService HechoService) {
        this.HechoService = HechoService;
    }

    @GetMapping("")
    public List<Hecho> obtenerHechos() {
        return HechoService.getHechos();
    }

    @GetMapping("/{metamapa}/colecciones/{id}")
    public List<Hecho> obtenerHechosXcoleccionXmetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccionX) {

        Handle id_coleccion = new Handle(id_coleccionX);
        return HechoService.getHechosXcoleccionXmetamapa(id_coleccion, metamapa);
    }

    @GetMapping("/{metamapa}/colecciones/{id}/modoNavegacion")

    public List<Hecho> obtenerHechosXcoleccionXmetamapaXfiltrado(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccionX,
            @RequestParam(name = "modoNavegacion") String modoNavegacion)
    {
        Handle id_coleccion = new Handle(id_coleccionX);
        return HechoService.getHechosXColeccionXmetampaXModoNavegacion(modoNavegacion,id_coleccion,metamapa);
    }

    @GetMapping("/{metamapa}/colecciones/{id}/categoria")
    public List<Hecho> obtenerHechosXcategoria(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable  ("id") Long id_coleccionX,
            @RequestParam("categoria") String categoria) {
        Handle id_coleccion = new Handle(id_coleccionX);
        return HechoService.getHechosXcategoriaXcolecciones(categoria,metamapa,id_coleccion);
    }

}
