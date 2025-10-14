package ar.utn.frba.ddsi.agregador.models.entities.colecciones;

import ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia.CriterioDePertenencia;
import ar.utn.frba.ddsi.agregador.dtos.output.HechoOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.FuenteOrigen;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import ar.utn.frba.ddsi.agregador.utils.HechoUtil;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(force = true)
@Getter
@Entity
@Table(name = "fuente")
public class Fuente{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "puerto")
    private String puerto;

    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://" + ip + ":" + puerto)
                .build();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre")
    private FuenteOrigen origenHechos;

    @JsonCreator
    public Fuente(@JsonProperty("ip") String ip, @JsonProperty("puerto") String puerto,@JsonProperty("id") Long id) { // el id no va
        this.id = id;
        this.ip = ip;
        this.puerto = puerto;
        this.origenHechos = this.determinarOrigen();
    }

    private FuenteOrigen determinarOrigen() {
        try {
            assert this.webClient() != null;
            String tipoServicio = this.webClient().get()
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
        WebClient.RequestHeadersUriSpec<?> requestSpec = this.webClient().get();

        if (criterios == null || criterios.isEmpty()) {
            return requestSpec
                    .uri("/api/hechos")
                    .retrieve()
                    .bodyToFlux(HechoOutputDTO.class)
                    .map(HechoUtil::hechoDTOtoHecho)
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
                .bodyToFlux(HechoOutputDTO.class)
                .map(HechoUtil::hechoDTOtoHecho)
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
            this.webClient()
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








