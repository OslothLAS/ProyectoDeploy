package ar.utn.frba.ddsi.agregador.models.repositories;

import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface IHechoRepository extends JpaRepository<Hecho, Long> {

    @Query("SELECT DISTINCT h FROM Hecho h " +
            "LEFT JOIN FETCH h.multimedia " +
            "LEFT JOIN FETCH h.categoria " +
            "LEFT JOIN FETCH h.ubicacion " +
            "JOIN h.colecciones c " +
            "WHERE c.id = :idColeccion " +
            "AND h.esValido = true")
    List<Hecho> findByColeccionIdAndEsValido(@Param("idColeccion") Long idColeccion);


    @Query("SELECT DISTINCT h FROM Hecho h " +
            "LEFT JOIN FETCH h.multimedia " +
            "LEFT JOIN FETCH h.categoria " +
            "LEFT JOIN FETCH h.ubicacion " +
            "WHERE h.esValido = true")
    List<Hecho> findAllWithRelations();

    @Query("SELECT h FROM Hecho h WHERE " +
            "h.titulo IN :titulos AND " +
            "h.descripcion IN :descripciones AND " +
            "h.fuenteOrigen IN :fuentes")
    List<Hecho> findPosiblesCoincidencias(
            @Param("titulos") List<String> titulos,
            @Param("descripciones") List<String> descripciones,
            @Param("fuentes") List<String> fuentes
    );

    Optional<Hecho> findByTituloAndDescripcion(String titulo, String descripcion);

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO( c.titulo, h.ubicacion.localidad.provincia.nombre,"+
            "NULL, NULL, NULL, COUNT(h), NULL) " +
            "FROM Hecho h " +
            "JOIN h.colecciones c " +
            "GROUP BY c.id, c.titulo, h.ubicacion.localidad.provincia.id, h.ubicacion.localidad.provincia.nombre " +
            "ORDER BY c.titulo ASC, COUNT(h) DESC")
    List<StatDTO> countHechosByProvinciaAndColeccion();


    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO(NULL, NULL,h.categoria.categoria, NULL, NULL, COUNT(h), NULL) " +
            "FROM Hecho h " +
            "GROUP BY h.categoria.categoria " +
            "ORDER BY COUNT(h) DESC")
    List<StatDTO> findCategoriaWithMostHechos();


    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO(NULL, NULL," +
            "h.categoria.categoria, " +
            "CAST(FUNCTION('HOUR', h.fechaHecho) AS integer), " +
            "NULL, COUNT(h), NULL) " +
            "FROM Hecho h " +
            "GROUP BY h.categoria.id, h.categoria.categoria, CAST(FUNCTION('HOUR', h.fechaHecho) AS integer) " +
            "ORDER BY h.categoria.categoria ASC, COUNT(h) DESC")
    List<StatDTO> findHoraWithMostHechosByCategoria();

    @Query("SELECT NEW ar.utn.frba.ddsi.agregador.dtos.output.StatDTO(NULL, h.ubicacion.localidad.provincia.nombre, " +
            "h.categoria.categoria, NULL, NULL, COUNT(h), NULL) " +
            "FROM Hecho h " +
            "GROUP BY h.ubicacion.localidad.provincia.nombre, h.categoria.categoria " +
            "ORDER BY h.categoria.categoria ASC, COUNT(h) DESC")
    List<StatDTO> findProvinciaWithMostHechosByCategoria();

    @Modifying
    @Transactional
    @Query("UPDATE Hecho h " +
            "SET h.esValido = false " +
            "WHERE h.titulo = :titulo " +
            "AND h.descripcion = :descripcion")
    int invalidateByTituloAndDescripcion(@Param("titulo") String titulo,
                                         @Param("descripcion") String descripcion);

    @Query("SELECT h FROM Hecho h JOIN h.colecciones c WHERE c = :coleccion")
    List<Hecho> findByColeccionesContaining(@Param("coleccion") ar.utn.frba.ddsi.agregador.models.entities.colecciones.Coleccion coleccion);

}
