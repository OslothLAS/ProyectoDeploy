package models.repositories;

import entities.hechos.Hecho;
import models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudEliminacionRepository {
    public List<SolicitudEliminacion> findAllSolicitudes();
    public void saveSolicitude(SolicitudEliminacion solicitud);
    public void deleteSolicitude(SolicitudEliminacion solicitud);

}
