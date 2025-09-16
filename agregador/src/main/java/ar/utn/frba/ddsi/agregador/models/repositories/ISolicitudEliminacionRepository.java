package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
    @Query("SELECT COUNT(s) FROM SolicitudEliminacion s JOIN s.estados e WHERE e.estado = 'RECHAZADA' AND e.spam = TRUE")
    Long countSolicitudesSpam();
}