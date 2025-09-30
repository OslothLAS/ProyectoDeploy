package ar.utn.ba.ddsi.fuenteProxy.controllers;

import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import ar.utn.ba.ddsi.fuenteProxy.services.ISolicitudService;
import ar.utn.ba.ddsi.fuenteProxy.services.impl.SolicitudService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/metamapa/solicitudes")
public class SolicitudController {

    private final ISolicitudService SolicitudService;
    private final SolicitudService solicitudService;

    public SolicitudController(ISolicitudService SolicitudService, SolicitudService solicitudService) {
        this.SolicitudService= SolicitudService;
        this.solicitudService=solicitudService;
    }

    @GetMapping("/{metamapa}")
    public List<SolicitudDto> obtenerSolicitudesXmetamapa(@PathVariable("metamapa") Long metamapa) {
        return SolicitudService.getSolicitudesXmetamapa(metamapa);
    }

    @PostMapping("/{metamapa}")
    public SolicitudDto subirSolicitudxMetamapa(@PathVariable("metamapa") Long metamapa,
                                                @RequestBody SolicitudesInputDto solicitud) {
        return solicitudService.postSolicitudesXmetamapa(metamapa, solicitud);
    }
}
