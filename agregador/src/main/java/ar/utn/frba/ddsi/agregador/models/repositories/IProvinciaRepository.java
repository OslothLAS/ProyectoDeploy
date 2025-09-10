package ar.utn.frba.ddsi.agregador.models.repositories;
import entities.hechos.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProvinciaRepository extends JpaRepository<Provincia, Long> {
    Provincia findByNombre(String nombre);
}
