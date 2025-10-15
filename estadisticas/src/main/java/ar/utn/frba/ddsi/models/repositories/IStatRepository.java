package ar.utn.frba.ddsi.models.repositories;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IStatRepository extends JpaRepository<Estadistica, Long> {
    // Pregunta: ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    // Busca la estadística más reciente del tipo "hechos_categoria" con la mayor cantidad.
    @Query("SELECT e FROM Estadistica e WHERE e.descripcion = 'hechos_categoria' ORDER BY e.fechaStat DESC, e.cantidad DESC")
    List<Estadistica> findTopCategoria(Pageable pageable);

    default Estadistica findTopCategoria() {
        List<Estadistica> result = findTopCategoria(PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }


    // Pregunta: De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos?
    // Busca la estadística más reciente para un título de colección específico, ordenada por cantidad.
    @Query("SELECT e FROM Estadistica e WHERE e.tituloColeccion = :tituloColeccion AND e.descripcion = 'hechos_provincia_coleccion' ORDER BY e.fechaStat DESC, e.cantidad DESC")
    List<Estadistica> findTopProvinciaByColeccion(@Param("tituloColeccion") String tituloColeccion, Pageable pageable);

    default Estadistica findTopProvinciaByColeccion(String tituloColeccion) {
        List<Estadistica> result = findTopProvinciaByColeccion(tituloColeccion, PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }


    // Pregunta: ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    @Query("SELECT e FROM Estadistica e WHERE e.categoria = :categoria AND e.descripcion = 'hechos_provincia_categoria' ORDER BY e.fechaStat DESC, e.cantidad DESC")
    List<Estadistica> findTopProvinciaByCategoria(@Param("categoria") String categoria, Pageable pageable);

    default Estadistica findTopProvinciaByCategoria(String categoria) {
        List<Estadistica> result = findTopProvinciaByCategoria(categoria, PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }


    // Pregunta: ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    @Query("SELECT e FROM Estadistica e WHERE e.categoria = :categoria AND e.descripcion = 'hechos_hora_categoria' ORDER BY e.fechaStat DESC, e.cantidad DESC")
    List<Estadistica> findTopHoraByCategoria(@Param("categoria") String categoria, Pageable pageable);

    default Estadistica findTopHoraByCategoria(String categoria) {
        List<Estadistica> result = findTopHoraByCategoria(categoria, PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }


    // Pregunta: ¿Cuántas solicitudes de eliminación son spam?
    @Query("SELECT e FROM Estadistica e WHERE e.descripcion = 'solicitudes_spam' ORDER BY e.fechaStat DESC")
    List<Estadistica> findLastSpamCount(Pageable pageable);

    default Estadistica findLastSpamCount() {
        List<Estadistica> result = findLastSpamCount(PageRequest.of(0, 1));
        return result.isEmpty() ? null : result.get(0);
    }
}