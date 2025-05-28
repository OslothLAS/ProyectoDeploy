package entities.colecciones;

import entities.Importador;
import entities.hechos.Hecho;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

public class Fuente{
    private final String url;
    private final WebClient webClient;

    public Fuente(String url) {
        this.url = url.startsWith("http") ? url : "http://localhost:8080/" + url;
        this.webClient = WebClient.builder().baseUrl(this.url).build();
    }
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








