package ar.utn.frba.ddsi.agregador.models.repositories;
import entities.hechos.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
    Optional<Ubicacion> findByLatitudAndLongitud(String latitud, String longitud);
}
