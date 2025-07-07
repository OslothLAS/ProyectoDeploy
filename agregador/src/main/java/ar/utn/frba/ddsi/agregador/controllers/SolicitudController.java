package ar.utn.frba.ddsi.agregador.controllers;


import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final ISolicitudEliminacionService solicitudService;

    public SolicitudController(ISolicitudEliminacionService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public void crearSolicitud(@RequestBody SolicitudInputDTO solicitud) {
        this.solicitudService.crearSolicitud(solicitud);
    }

    @GetMapping("/{idSolicitud}")
    public void getSolicitud(@PathVariable("idSolicitud") Long idSolicitud) {
        this.solicitudService.getSolicitud(idSolicitud);
    }

    @PostMapping("/{id_solicitud}/aceptar")
    public void aceptarSolicitud(@PathVariable("id_solicitud") Long idSolicitud) {this.solicitudService.aceptarSolicitud(idSolicitud);}

    @PostMapping("/{id_solicitud}/rechazar")
    public void rechazarSolicitud(@PathVariable("id_solicitud") Long idSolicitud) {this.solicitudService.rechazarSolicitud(idSolicitud);}



}