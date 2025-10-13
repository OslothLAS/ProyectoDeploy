package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ILocalidadRepository extends JpaRepository<Localidad, Long> {
    Optional<Localidad> findByNombreAndProvinciaNombre(String nombre, String provinciaNombre);
}
