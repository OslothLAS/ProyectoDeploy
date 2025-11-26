package ar.utn.frba.ddsi.agregador;

import ar.utn.frba.ddsi.agregador.config.FuenteConfig;
import ar.utn.frba.ddsi.agregador.config.FuentesProperties;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import ar.utn.frba.ddsi.agregador.models.entities.normalizador.OpenStreetMap;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(FuentesProperties.class)
public class AgregadorApplication {

    @PersistenceContext
    private EntityManager entityManager;
    @Value("${fuenteEstatica.service.url}")
    private String fuenteEstaticaUrl;
    @Value("${fuenteDinamica.service.url}")
    private String fuenteDinamicaUrl;
    @Value("${fuenteProxy.service.url}")
    private String fuenteProxyUrl;

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
    CommandLineRunner importarHechosFuentes(IHechoRepository hechoRepository, IColeccionService coleccionService, @Qualifier("fuentesProperties") FuentesProperties fuentesProperties) {
        return args -> {

            List<Fuente> fuentes = List.of(
                    new Fuente(fuenteEstaticaUrl),
                    new Fuente(fuenteDinamicaUrl),
                    new Fuente(fuenteProxyUrl)
            );


            List<CriterioDePertenencia> criterios = new ArrayList<>();

            for (FuenteConfig fuenteConfig : fuentesProperties.getFuentes()) {
                Fuente fuente = new Fuente(fuenteConfig.getIp(), fuenteConfig.getPuerto(), null);
                try {
                    List<Hecho> hechos = fuente.obtenerHechos(criterios);
                    coleccionService.normalizarHechos(hechos);

                    if (hechos != null && !hechos.isEmpty()) {
                        hechoRepository.saveAll(hechos);

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            System.out.println("Importación inicial de hechos finalizada.");
        };
    }
}
