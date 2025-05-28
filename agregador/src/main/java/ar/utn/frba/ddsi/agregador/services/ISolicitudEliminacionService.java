package ar.utn.frba.ddsi.agregador.services;

import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;

public interface ISolicitudEliminacionService {

    public void crearSolicitud(String justificacion, Long idHecho, Contribuyente solicitante);

    public String validarJustificacion(String justificacionSolicitud);
}
