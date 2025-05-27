package services.impl;

import models.entities.usuarios.Contribuyente;
import models.entities.solicitudes.SolicitudEliminacion;
import services.ISolicitudEliminacionService;

import java.util.List;

public class SolicitudEliminacionService implements ISolicitudEliminacionService {


    @Override
    public void crearSolicitud(String justificacion, Long idHecho, Contribuyente solicitante) {
        this.validarJustificacion(justificacion);
        SolicitudEliminacion solicitud = new SolicitudEliminacion(justificacion, idHecho, solicitante);
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
