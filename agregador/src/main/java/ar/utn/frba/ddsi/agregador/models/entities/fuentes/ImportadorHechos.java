package ar.utn.frba.ddsi.agregador.models.entities.fuentes;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ImportadorHechos implements Importador{
    private String ip;
    private String puerto;

    @Override
    public List<Hecho> obtenerHechos() {
        String url = String.format("http://%s:%s/hechos", ip, puerto);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<List<Hecho>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener hechos: " + e.getMessage());
            return List.of(); //lanzar excepción (sería lo mejor)
        }
    }

    public ImportadorHechos(String ip, String puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }
}
