package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudRepository {
    void save(SolicitudEliminacion solicitud);
    Optional<SolicitudEliminacion> findById(Long id);
    List<SolicitudEliminacion> findAll();
    List<SolicitudEliminacion> findByEstado(EstadoSolicitudEliminacion estado);
}
