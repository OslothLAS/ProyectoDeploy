package ar.utn.frba.ddsi.models.entities;

import ar.utn.frba.ddsi.dtos.StatDTO;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

public class AgregadorConnector {
    private final String ip;
    private final String puerto;
    private final WebClient webClient;

    public AgregadorConnector() {
        this.puerto = "8080";
        this.ip = "localhost";
        this.webClient = WebClient.builder().baseUrl("http://" + ip + ":" + puerto).build();
    }

    public List<StatDTO> getHechosDeColeccion(Long idColeccion) {
        try {
            return webClient.get()
                    .uri("/stats/colecciones/provincias-top", idColeccion)
                    .retrieve()
                    .bodyToFlux(StatDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<StatDTO> getCategoriaPorHechos() {
        try {
            return webClient.get()
                    .uri("/stats/categorias-top")
                    .retrieve()
                    .bodyToFlux(StatDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<StatDTO> getProviniciaCategoriaReportada() {
        try {
            return webClient.get()
                    .uri("/stats/categorias/provincias-top")
                    .retrieve()
                    .bodyToFlux(StatDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<StatDTO> getHoraMasReportada() {
        try {
            return webClient.get()
                    .uri("/stats/categorias/horas-top")
                    .retrieve()
                    .bodyToFlux(StatDTO.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public StatDTO getSpam() {
        try {
            return webClient.get()
                    .uri("/stats/solicitudes/spam")
                    .retrieve()
                    .bodyToMono(StatDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
