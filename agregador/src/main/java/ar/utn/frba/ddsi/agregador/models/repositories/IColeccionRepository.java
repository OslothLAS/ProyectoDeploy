package ar.utn.frba.ddsi.agregador.models.repositories;


import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {

    boolean existsByTitulo(String titulo);

    /*void save(Coleccion coleccion);
    Optional<Coleccion> findById(Long id);
    List<Coleccion> findAll();

    boolean delete(Long id);
     */
}
