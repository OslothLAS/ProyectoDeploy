package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.UsuarioDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionService {

    SolicitudOutputDTO crearSolicitud(SolicitudInputDTO solicitud);
    SolicitudEliminacion getSolicitud(Long solicitante);
    List<SolicitudOutputDTO> getSolicitudes();
    List<SolicitudOutputDTO> getSolicitudesPorUsuario(String solicitante);
    UsuarioDTO obtenerUserPorUsername(String username);
    String validarJustificacion(String justificacionSolicitud);
    void aceptarSolicitud(Long idSolicitud);
    void rechazarSolicitud(Long idSolicitud);
    StatDTO getCantidadSpam();
}