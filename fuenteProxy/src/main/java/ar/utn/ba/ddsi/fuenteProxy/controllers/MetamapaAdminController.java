package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionInputDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Handle;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaAdminService;
import ar.utn.ba.ddsi.fuenteProxy.services.impl.MetamapaAdminService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/metamapaADM")
public class MetamapaAdminController {

    private final IMetamapaAdminService MetemapaAdminService;
    private final MetamapaAdminService metamapaAdminService;

    public MetamapaAdminController(IMetamapaAdminService MetemapaAdminService, MetamapaAdminService metamapaService) {
        this.MetemapaAdminService = MetemapaAdminService;
        this.metamapaAdminService = metamapaService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos() {
        return MetemapaAdminService.getHechos();
    }


    @GetMapping("/{metamapa}/hechos/colecciones/{id}")
    public List<Hecho> obtenerHechosXcoleccionXmetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccionX) {

        Handle id_coleccion = new Handle(id_coleccionX);
        return MetemapaAdminService.getHechosXcoleccionXmetamapa(id_coleccion, metamapa);
    }

    @GetMapping("/{metamapa}/colecciones")
    public List<ColeccionDto> obtenerColeccionesXMetamapa(
            @PathVariable("metamapa") Long metamapa) {
        return MetemapaAdminService.getColeccionesXmetamapa(metamapa);
    }

    @PostMapping("/{metamapa}/colecciones")
    public ColeccionDto subirColeccionesXMetamapa(
            @PathVariable("metamapa") Long metamapa, @RequestBody ColeccionInputDto inputColeccion) {
        return MetemapaAdminService.postColeccionesXmetamapa(metamapa, inputColeccion);
    }

    @PutMapping("/{metamapa}/colecciones/{id}")
    public ColeccionDto actualizarColeccionXMetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccion,
            @RequestBody ColeccionInputDto inputColeccion) {
        return MetemapaAdminService.putColeccionesXmetamapa(metamapa,id_coleccion,inputColeccion);
    }

    @DeleteMapping("/{metamapa}/colecciones/{id}")
    public void eliminarColeccionXMetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_coleccion
    ) {
    }



    @GetMapping("/{metamapa}/hechos")
    public List<Hecho> obtenerHechosXMetamapa(@PathVariable("metamapa") Long id) {
        return MetemapaAdminService.getHechosXmetamapa(id);
    }

    @GetMapping("/{metamapa}/solicitudes")
    public List<SolicitudDto> obtenerSolicitudesXmetamapa(@PathVariable("metamapa") Long metamapa) {
        return MetemapaAdminService.getSolicitudesXmetamapa(metamapa);
    }

    @PostMapping("/{metamapa}/solicitudes")
    public SolicitudDto subirSolicitudxMetamapa(@PathVariable("metamapa") Long metamapa,
                                                @RequestBody SolicitudesInputDto solicitud) {
        return metamapaAdminService.postSolicitudesXmetamapa(metamapa, solicitud);
    }

    @PutMapping("/{metamapa}/solicitudes/{id}/actualizar")
    public SolicitudDto actualizarSolicitudxMetamapa(
            @PathVariable("metamapa") Long metamapa,
            @PathVariable("id") Long id_solicitud,
            @RequestBody String estado) {
        return metamapaAdminService.putSolicitudesXmetamapa(metamapa, id_solicitud, estado);
    }
}
