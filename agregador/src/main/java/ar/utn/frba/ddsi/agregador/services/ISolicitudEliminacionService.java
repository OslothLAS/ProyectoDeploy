package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import entities.solicitudes.SolicitudEliminacion;

public interface ISolicitudEliminacionService {

    Long crearSolicitud(SolicitudInputDTO solicitud);
    SolicitudEliminacion getSolicitud(Long idSolicitud);
    String validarJustificacion(String justificacionSolicitud);
    void aceptarSolicitud(Long idSolicitud);
    void rechazarSolicitud(Long idSolicitud);
    StatDTO getCantidadSpam();
}