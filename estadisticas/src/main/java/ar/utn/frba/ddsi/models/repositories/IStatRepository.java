package ar.utn.frba.ddsi.models.repositories;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.models.entities.TipoEstadistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IStatRepository extends JpaRepository<Estadistica, Long> {
    List<Estadistica> findTop1ByTipoEstadisticaOrderByFechaGeneracionDesc(TipoEstadistica tipoEstadistica);
}
