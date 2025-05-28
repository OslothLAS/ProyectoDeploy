package entities.colecciones;

import entities.Importador;
import entities.hechos.Hecho;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

public class Fuente implements Importador {
    private final String url;
    private final WebClient webClient;
    public Fuente() {
        this.url = "http://localhost:8080/fuentedinamica/hechos";
        this.webClient = WebClient.builder().baseUrl(this.url).build();
    }
    @Override
    public List<Hecho> obtenerHechos() {
         List<Hecho> hechos = webClient.get()
                .uri(this.url)
                .retrieve()
                .bodyToFlux(Hecho.class)
                .collectList()
                .block();
         return hechos;
    }
}








