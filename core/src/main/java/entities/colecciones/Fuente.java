package entities.colecciones;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;
import java.util.Map;

public class Fuente{
    private final WebClient webClient;
    @Getter
    private final Origen origenHechos;

    @Getter
    private final long id;

    public Fuente(String ip, String puerto, Origen origenHechos, Long id) {
        this.id = id;
        this.origenHechos = origenHechos;
        this.webClient = WebClient.builder().baseUrl("http://" +ip+ ":" +puerto).build();
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

        return requestSpec
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
}








