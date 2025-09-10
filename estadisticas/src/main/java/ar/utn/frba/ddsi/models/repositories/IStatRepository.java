package ar.utn.frba.ddsi.models.repositories;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.models.entities.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatRepository extends JpaRepository<Estadistica, Long> {
}
