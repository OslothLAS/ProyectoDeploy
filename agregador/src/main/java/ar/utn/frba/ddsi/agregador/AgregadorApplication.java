package ar.utn.frba.ddsi.agregador;

import ar.utn.frba.ddsi.agregador.dtos.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.normalizador.OpenStreetMap;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class AgregadorApplication {

    @PersistenceContext
    private EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(AgregadorApplication.class, args);

        OpenStreetMap osm = new OpenStreetMap();

        // Ejemplo 1: Obelisco de Buenos Aires
        double lat1 = -34.914536;
        double lon1 = -060.035774;
        List<String> resultado1 = osm.obtenerUbicacion(lat1, lon1);
        System.out.println("Chivilcoy en alerta por Emanación de gas tóxico");
        System.out.println("Buenos Aires -> " + resultado1.get(0));
        System.out.println("Chivilcoy -> " + resultado1.get(1));
    }

    @Bean
    @Transactional
    public CommandLineRunner inicializarConstraint() {
        return args -> {
            try {
                entityManager.createNativeQuery("""
                    ALTER TABLE hecho_coleccion
                    ADD CONSTRAINT FK_hecho_coleccion_hecho
                    FOREIGN KEY (hecho_id) REFERENCES hecho(id)
                    ON DELETE CASCADE
                """).executeUpdate();
                System.out.println("Constraint creada correctamente (ON DELETE CASCADE).");
            } catch (Exception e) {
                System.out.println("No se pudo crear la constraint (posiblemente ya exista): " + e.getMessage());
            }
        };
    }


    @Bean
    CommandLineRunner crearColeccionMaestra(IColeccionService coleccionService) {
        return args -> {
            try {
                ColeccionInputDTO dto = new ColeccionInputDTO();
                dto.setTitulo("coleccion maestra");
                dto.setDescripcion("para importar los hechos a agregador");
                dto.setEstrategiaConsenso("MULTIPLE_MENCION");

                List<Fuente> fuentes = new ArrayList<>();
                fuentes.add(new Fuente("localhost", "8060", null));
                fuentes.add(new Fuente("localhost", "8070", null));
                // fuentes.add(new Fuente("localhost", "8090",null));
                dto.setFuentes(fuentes);

                dto.setCriterios(new ArrayList<CriterioDePertenencia>());

                coleccionService.createColeccion(dto);
                System.out.println("Colección maestra creada exitosamente al iniciar el sistema.");
            } catch (Exception e) {
                coleccionService.actualizarHechos();
                System.out.println("No se creó la colección maestra (puede que ya exista): " + e.getMessage());
            }
        };
    }
}