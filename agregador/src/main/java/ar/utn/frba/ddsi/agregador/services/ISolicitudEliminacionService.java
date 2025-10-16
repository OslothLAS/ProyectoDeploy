package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionService {

    Long crearSolicitud(SolicitudInputDTO solicitud);
    SolicitudEliminacion getSolicitud(Long solicitante);
    List<SolicitudOutputDTO> getSolicitudes();
    String validarJustificacion(String justificacionSolicitud);
    void aceptarSolicitud(Long idSolicitud);
    void rechazarSolicitud(Long idSolicitud);
    StatDTO getCantidadSpam();
}