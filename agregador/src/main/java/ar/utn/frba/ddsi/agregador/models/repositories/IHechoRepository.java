package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    /*void save(Hecho hecho);
    Hecho findById(Long id);
    List<Hecho> findAll();*/
}
