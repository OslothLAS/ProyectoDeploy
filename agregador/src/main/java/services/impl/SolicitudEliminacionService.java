package services.impl;

import models.entities.usuarios.Contribuyente;
import models.entities.solicitudes.SolicitudEliminacion;
import models.repositories.ISolicitudEliminacionRepository;
import services.ISolicitudEliminacionService;

import java.util.List;

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
