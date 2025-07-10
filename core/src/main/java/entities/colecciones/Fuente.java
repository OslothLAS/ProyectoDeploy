package entities.colecciones;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.FuenteOrigen;
import entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;

public class Fuente{
    private final WebClient webClient;
    @Getter
    private final FuenteOrigen origenHechos;

    @Getter
    private final long id;

    @JsonCreator
    public Fuente(@JsonProperty("ip") String ip, @JsonProperty("puerto") String puerto,@JsonProperty("id") Long id) { // el id no va
        this.id = id;
        this.webClient = WebClient.builder().baseUrl("http://" +ip+ ":" +puerto).build();
        this.origenHechos = this.determinarOrigen();
    }

    private FuenteOrigen determinarOrigen() {
        try {
            assert webClient != null;
            String tipoServicio = webClient.get()
                    .uri("/api/hechos/origen") // Endpoint que debe existir en cada servicio
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            assert tipoServicio != null;
            return switch (tipoServicio) {
                case "DINAMICO" -> FuenteOrigen.DINAMICO;
                case "ESTATICO" -> FuenteOrigen.ESTATICO;
                case "PROXY" -> FuenteOrigen.PROXY;
                default -> FuenteOrigen.DESCONOCIDO;
            };
        } catch (Exception e) {
            return FuenteOrigen.DESCONOCIDO;
        }
    }

    public List<Hecho> obtenerHechos(List<CriterioDePertenencia> criterios) {
        WebClient.RequestHeadersUriSpec<?> requestSpec = webClient.get();

        if (criterios == null || criterios.isEmpty()) {
            return requestSpec
                    .uri("/api/hechos")
                    .retrieve()
                    .bodyToFlux(Hecho.class)
                    .collectList()
                    .block();
        }
        List<Hecho> hechos = requestSpec
                .uri(uriBuilder -> {
                    MultiValueMap<String, String> queryParams = this.construirQueryParams(criterios);
                    return uriBuilder
                            .path("/api/hechos")
                            .queryParams(queryParams)
                            .build();
                })
                .retrieve()
                .bodyToFlux(Hecho.class)
                .collectList()
                .block();

        assert hechos != null;
        hechos.forEach(h -> h.setFuenteOrigen(this.origenHechos));
        return hechos;
    }

    private MultiValueMap<String, String> construirQueryParams(List<CriterioDePertenencia> criterios) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (CriterioDePertenencia criterio : criterios) {
            MultiValueMap<String, String> map = criterio.aQueryParam();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                for (String valor : entry.getValue()) {
                    params.add(entry.getKey(), valor);
                }
            }
        }
        return params;
    }

    public void invalidarHecho(String titulo, String descripcion) {
        try {
            this.webClient
                    .put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/hechos/invalidar")
                            .queryParam("titulo", titulo)
                            .queryParam("descripcion", descripcion)
                            .build())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            System.err.println("No se pudo invalidar hecho con título: " + titulo + " y descripción: " + descripcion +
                    " en la fuente " + origenHechos);
            e.printStackTrace();
        }
    }
}








