package controllers;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.services.ISolicitudService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/fuentedinamica/solicitudes")
public class SolicitudController {
    private final ISolicitudService solicitudService;
    public SolicitudController(ISolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }
    @PostMapping
    public void createSolicitud(SolicitudInputDTO solicitud) {
        this.solicitudService.createSolicitud(solicitud);
    }

}
