package models.repositories;

import entities.hechos.Hecho;
import models.entities.solicitudes.EstadoSolicitudEliminacion;
import models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionRepository {
    void save(SolicitudEliminacion solicitud);
    Optional<SolicitudEliminacion> findById(Long id);
    List<SolicitudEliminacion> findAll();
    List<SolicitudEliminacion> findByEstado(EstadoSolicitudEliminacion estado);
    void deleteSolicitude(SolicitudEliminacion solicitud);

}
