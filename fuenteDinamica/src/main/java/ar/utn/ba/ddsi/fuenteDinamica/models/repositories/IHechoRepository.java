package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    Optional<Hecho> findByDatosHechosTituloAndDatosHechosDescripcion(String titulo, String descripcion);
}
