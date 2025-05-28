package entities.colecciones;

import entities.hechos.Hecho;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

public class Fuente{
    private final WebClient webClient;

    public Fuente(String ip, String puerto) {
        this.webClient = WebClient.builder().baseUrl("http://" +ip+ ":" +puerto).build();
    }

    public List<Hecho> obtenerHechos() {
//        System.out.println(webClient.get()
//                .uri("/hechos")
//                .retrieve().bodyToMono(String.class).block());

        return webClient.get()
               .uri("/hechos")
               .retrieve()
               .bodyToFlux(Hecho.class)
               .collectList()
               .block();
    }
}








