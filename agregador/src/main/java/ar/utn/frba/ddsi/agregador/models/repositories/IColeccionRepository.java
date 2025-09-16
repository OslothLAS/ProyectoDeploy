package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.colecciones.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
    /*void save(Coleccion coleccion);
    Optional<Coleccion> findById(Long id);
    List<Coleccion> findAll();

    boolean delete(Long id);
     */
}
