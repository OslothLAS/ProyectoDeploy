package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {

}
