package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProvinciaRepository extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByNombre(String nombre);
}
