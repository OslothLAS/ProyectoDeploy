package ar.utn.frba.ddsi.agregador.controllers;


import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ar.utn.frba.ddsi.agregador.utils.SolicitudUtil.solicitudToDTO;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final ISolicitudEliminacionService solicitudService;

    public SolicitudController(ISolicitudEliminacionService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CONTRIBUYENTE', 'ADMIN')")
    public SolicitudOutputDTO crearSolicitud(@RequestBody SolicitudInputDTO solicitud) {
        return this.solicitudService.crearSolicitud(solicitud);
    }

    @GetMapping("/{idSolicitud}")
    @PreAuthorize("hasAnyRole('ADMIN','CONTRIBUYENTE')")
    public SolicitudOutputDTO getSolicitud(@PathVariable("idSolicitud") Long solicitante) {
        return solicitudToDTO(this.solicitudService.getSolicitud(solicitante));
    }

    @PutMapping("/{id_solicitud}/aceptar")
    @PreAuthorize("hasRole('ADMIN')")
    public void aceptarSolicitud(@PathVariable("id_solicitud") Long idSolicitud) {this.solicitudService.aceptarSolicitud(idSolicitud);}

    @PutMapping("/{id_solicitud}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public void rechazarSolicitud(@PathVariable("id_solicitud") Long idSolicitud) {this.solicitudService.rechazarSolicitud(idSolicitud);}

    @GetMapping("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudOutputDTO>> getSolicitudes(){

        return ResponseEntity.ok(this.solicitudService.getSolicitudes());
    }

}