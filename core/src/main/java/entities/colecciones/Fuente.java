package entities.colecciones;

import entities.hechos.Hecho;
import entities.hechos.Origen;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

public class Fuente{
    private final WebClient webClient;
    @Getter
    private final Origen origenHechos;

    public Fuente(String ip, String puerto, Origen origenHechos) {
        this.origenHechos = origenHechos;
        this.webClient = WebClient.builder().baseUrl("http://" +ip+ ":" +puerto).build();
    }

    public List<Hecho> obtenerHechos() {
        return webClient.get()
               .uri("/api/hechos")
               .retrieve()
               .bodyToFlux(Hecho.class)
               .collectList()
               .block();
    }
}








