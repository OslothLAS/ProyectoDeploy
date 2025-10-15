package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    Fuente findByPuerto(String puerto);
}
