package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionRepository {

    void save(SolicitudEliminacion solicitud);

    Optional<SolicitudEliminacion> findById(Long id);

    List<SolicitudEliminacion> findAll();

    List<SolicitudEliminacion> findByEstado(EstadoSolicitudEliminacion estado);

    public void deleteSolicitudes(SolicitudEliminacion solicitud);

}
