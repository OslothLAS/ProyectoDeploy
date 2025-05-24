package services;

import entities.usuarios.Contribuyente;
import models.entities.solicitudes.SolicitudEliminacion;
import java.util.List;

public interface ISolicitudEliminacionService {

    public void crearSolicitud(String justificacion, Long idHecho, Long solicitante);
}
