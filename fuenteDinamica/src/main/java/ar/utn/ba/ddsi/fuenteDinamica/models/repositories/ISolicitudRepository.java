package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;


import entities.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudRepository extends JpaRepository<SolicitudEliminacion, Long>{
}
