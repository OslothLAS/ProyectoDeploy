package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;

public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    ISolicitudEliminacionRepository  solicitudRepository;

    @Override
    public void crearSolicitud(String justificacion, Long idHecho, Contribuyente solicitante) {
        this.validarJustificacion(justificacion);
        SolicitudEliminacion solicitud = new SolicitudEliminacion(justificacion, idHecho, solicitante);
        solicitudRepository.save(solicitud);
    }

    @Override
    public String validarJustificacion(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            return justificacionSolicitud;
        }

    }
}
