package ar.utn.frba.ddsi.agregador;

import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import ar.utn.frba.ddsi.agregador.models.entities.normalizador.OpenStreetMap;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
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
    CommandLineRunner importarHechosFuentes(IHechoRepository hechoRepository, IColeccionService coleccionService) {
        return args -> {
            List<Fuente> fuentes = List.of(
                    new Fuente("localhost", "8060", null)
            );
/*
,
                    new Fuente("localhost", "8070", null),
                    new Fuente("localhost", "8090", null)
 */
            List<CriterioDePertenencia> criterios = new ArrayList<>();

            for (Fuente fuente : fuentes) {
                try {
                    List<Hecho> hechos = fuente.obtenerHechos(criterios);

                    coleccionService.normalizarHechos(hechos);

                    if (hechos != null && !hechos.isEmpty()) {
                        hechoRepository.saveAll(hechos);
                        System.out.println("Importados " + hechos.size() +
                                " hechos desde " + fuente.getIp() + ":" + fuente.getPuerto());
                    }

                } catch (Exception e) {
                    System.out.println("No se pudieron importar hechos desde " +
                            fuente.getIp() + ":" + fuente.getPuerto());
                    e.printStackTrace();
                }
            }

            System.out.println("Importación inicial de hechos finalizada.");
        };
    }
}
