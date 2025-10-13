package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IProvinciaRepository extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByNombre(String nombre);
}
