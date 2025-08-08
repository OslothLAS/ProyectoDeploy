package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.solicitudes.PosibleEstadoSolicitud;
import entities.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionRepository {

    Long save(SolicitudEliminacion solicitud);

    Optional<SolicitudEliminacion> findById(Long id);

    List<SolicitudEliminacion> findAll();

    List<SolicitudEliminacion> findByEstado(PosibleEstadoSolicitud estado);

    void deleteSolicitud(SolicitudEliminacion solicitud);

}