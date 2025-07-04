package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.services.IMetamapaService;
import ar.utn.ba.ddsi.fuenteProxy.services.impl.MetamapaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metamapa/solicitudes")
public class SolicitudController {

    private final IMetamapaService MetemapaService;
    private final MetamapaService metamapaService;

    public SolicitudController(IMetamapaService MetemapaService, MetamapaService metamapaService) {
        this.MetemapaService=MetemapaService;
        this.metamapaService=metamapaService;
    }

    @GetMapping("/{metamapa}")
    public List<SolicitudDto> obtenerSolicitudesXmetamapa(@PathVariable("metamapa") Long metamapa) {
        return MetemapaService.getSolicitudesXmetamapa(metamapa);
    }

    @PostMapping("/{metamapa}")
    public SolicitudDto subirSolicitudxMetamapa(@PathVariable("metamapa") Long metamapa,
                                                @RequestBody SolicitudesInputDto solicitud) {
        return metamapaService.postSolicitudesXmetamapa(metamapa, solicitud);
    }
}
