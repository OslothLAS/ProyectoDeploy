package ar.utn.ba.ddsi.fuenteDinamica.models.repositories;

import entities.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByCategoriaNormalizada(String clave);
}
