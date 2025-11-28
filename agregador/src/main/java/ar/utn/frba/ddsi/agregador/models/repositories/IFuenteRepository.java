package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    Fuente findByUrl(String url);

    List<Fuente> findAllByUrlIn(List<String> url);
}
