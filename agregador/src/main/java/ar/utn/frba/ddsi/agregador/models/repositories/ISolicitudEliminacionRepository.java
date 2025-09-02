package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import entities.solicitudes.PosibleEstadoSolicitud;
import entities.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {


    /*Long save(SolicitudInputDTO solicitud);

    Optional<SolicitudEliminacion> findById(Long id);

    List<SolicitudEliminacion> findAll();

    List<SolicitudEliminacion> findByEstado(PosibleEstadoSolicitud estado);

    void deleteSolicitud(SolicitudEliminacion solicitud);*/

}