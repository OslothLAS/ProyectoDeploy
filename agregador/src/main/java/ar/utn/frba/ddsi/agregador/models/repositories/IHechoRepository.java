package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import entities.hechos.Hecho;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO('', h.datosHechos.ubicacion.localidad.provincia.nombre, COUNT(h)) " +
            "FROM Hecho h JOIN h.colecciones c " +
            "WHERE c.id = :idColeccion " +
            "GROUP BY h.datosHechos.ubicacion.localidad.provincia.nombre " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> countHechosByProvinciaAndColeccion(@Param("idColeccion") Long idColeccion);

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO('CATEGORIA', h.datosHechos.categoria.categoria, COUNT(h)) " +
            "FROM Hecho h " +
            "GROUP BY h.datosHechos.categoria.categoria " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> findCategoriaWithMostHechos();

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO('HORA', " +
            "CONCAT('HORA: ', CAST(FUNCTION('HOUR', h.datosHechos.fechaHecho) AS string)), " +
            "COUNT(h)) " +
            "FROM Hecho h " +
            "GROUP BY FUNCTION('HOUR', h.datosHechos.fechaHecho) " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> findHoraWithMostHechosByCategoria();

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO('PROVINCIA', h.datosHechos.ubicacion.localidad.provincia.nombre, COUNT(h)) " +
            "FROM Hecho h " +
            "GROUP BY h.datosHechos.ubicacion.localidad.provincia.nombre " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> findProvinciaWithMostHechosByCategoria();

    @Modifying
    @Transactional
    @Query("UPDATE Hecho h " +
            "SET h.esValido = false " +
            "WHERE h.datosHechos.titulo = :titulo " +
            "AND h.datosHechos.descripcion = :descripcion")
    int invalidateByTituloAndDescripcion(@Param("titulo") String titulo,
                                         @Param("descripcion") String descripcion);
}
