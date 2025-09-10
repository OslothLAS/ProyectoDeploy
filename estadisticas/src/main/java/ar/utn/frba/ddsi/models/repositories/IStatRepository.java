package ar.utn.frba.ddsi.models.repositories;

import ar.utn.frba.ddsi.dtos.StatDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatRepository extends JpaRepository<StatDTO, Long> {
}
