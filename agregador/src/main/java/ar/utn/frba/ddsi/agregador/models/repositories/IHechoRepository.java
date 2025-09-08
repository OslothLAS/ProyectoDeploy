package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {
    @Query("SELECT NEW StatDTO(h.datosHechos.ubicacion.provincia, COUNT(h)) " +
            "FROM Hecho h JOIN h.colecciones c " +
            "WHERE c.id = :idColeccion " +
            "GROUP BY h.datosHechos.ubicacion.provincia " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> countHechosByProvinciaAndColeccion(@Param("idColeccion") Long idColeccion);
}
