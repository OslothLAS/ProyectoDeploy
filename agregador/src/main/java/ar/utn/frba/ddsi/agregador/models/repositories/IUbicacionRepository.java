package ar.utn.frba.ddsi.agregador.models.repositories;
import entities.hechos.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Set;

public interface IUbicacionRepository extends JpaRepository<Ubicacion, Long> {
    @Query("SELECT u FROM Ubicacion u WHERE CONCAT(CAST(u.latitud AS string), ',', CAST(u.longitud AS string)) IN :coordenadasClave")
    List<Ubicacion> findByLatitudAndLongitudIn(@Param("coordenadasClave") Set<String> coordenadasClave);
}
