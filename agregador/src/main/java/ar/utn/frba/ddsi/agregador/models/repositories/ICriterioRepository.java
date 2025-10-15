package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICriterioRepository extends JpaRepository<CriterioDePertenencia, Long> {
}
