package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import ar.utn.ba.ddsi.fuenteProxy.services.impl.MetamapaService;
import entities.colecciones.Handle;
import entities.hechos.Hecho;
import entities.solicitudes.SolicitudEliminacion;
import org.springframework.web.bind.annotation.*;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiService;

import java.util.List;

@RestController
@RequestMapping("/metamapa")
public class MetamapaController {

    private final IMetamapaService MetemapaService;
    private final MetamapaService metamapaService;

    public MetamapaController(IMetamapaService MetemapaService, MetamapaService metamapaService) {
        this.MetemapaService = MetemapaService;
        this.metamapaService = metamapaService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos() {
        return MetemapaService.getHechos();
    }

    @GetMapping("/colecciones/{metamapa}/{id}")
    public List<Hecho> obtenerHechosXcoleccionXmetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccionX) {

        Handle id_coleccion = new Handle(id_coleccionX);
        return MetemapaService.getHechosXcoleccionXmetamapa(id_coleccion, metamapa);
    }

    @GetMapping("/colecciones/{metamapa}")
    public List<ColeccionDto> obtenerColeccionesXMetamapa(
        @PathVariable("metamapa") Long metamapa){
        return MetemapaService.getColeccionesXmetamapa(metamapa);
    }

    @GetMapping("/hechos/categoria")
    public List<Hecho> obtenerHechosXcategoria(@RequestParam("categoria") String categoria) {
        return MetemapaService.getHechosXcategoria(categoria);
    }

    @GetMapping("/hechos/{id}")
    public List<Hecho> obtenerHechosXMetamapa(@PathVariable("id") Long id) {
        return MetemapaService.getHechosXmetamapa(id);
    }

    @GetMapping("/solicitudes/{id}")
    public List<SolicitudDto> obtenerSolicitudesXmetamapa(@PathVariable("id") Long metamapa) {
        return MetemapaService.getSolicitudesXmetamapa(metamapa);
    }

    @PostMapping("/solicitudes/{metamapa}")
    public SolicitudDto subirSolicitudxMetamapa(@PathVariable("metamapa") Long metamapa,
                                        @RequestBody SolicitudesInputDto solicitud) {
        return metamapaService.postSolicitudesXmetamapa(metamapa, solicitud);
    }
}




