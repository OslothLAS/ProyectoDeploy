package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import entities.hechos.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProvinciaRepository extends JpaRepository<Provincia, Long> {
}
