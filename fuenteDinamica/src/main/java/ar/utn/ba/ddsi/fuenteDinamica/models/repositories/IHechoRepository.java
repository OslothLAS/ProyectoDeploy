package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    Optional<Hecho> findByTituloAndDescripcion(String titulo, String descripcion);
    HechoDTO findById(long id);
    List<Hecho> findAll();
    List<Hecho> findByUsername(String username);
}
