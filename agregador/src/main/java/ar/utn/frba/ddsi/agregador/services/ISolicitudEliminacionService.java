package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;

public interface ISolicitudEliminacionService {

    public void crearSolicitud(SolicitudInputDTO solicitud);
    public SolicitudEliminacion getSolicitud(Long idSolicitud);
    public String validarJustificacion(String justificacionSolicitud);

}