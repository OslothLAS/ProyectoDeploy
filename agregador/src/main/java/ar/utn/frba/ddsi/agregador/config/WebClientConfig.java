package ar.utn.frba.ddsi.agregador.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;


@Configuration
public class WebClientConfig {

    @Value("${servicioDeUsuarios.service.url}")
    private String usuarioServiceUrl;

    @Value("${fuenteDinamica.service.url}")
    private String hechosServiceUrl;

    // Si necesitás más adelante:
    @Value("${estadisticas.service.url}")
    private String estadisticasServiceUrl;

    // 1) /api/users/username
    @Bean
    public WebClient usuarioPorSolicitudWebClient() {
        return WebClient.builder()
                .baseUrl(usuarioServiceUrl + "/api/users/username")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 2) /api/users
    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl(usuarioServiceUrl + "/api/users")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 3) /api/users/me
    @Bean
    public WebClient userNameWebClient() {
        return WebClient.builder()
                .baseUrl(usuarioServiceUrl + "/api/users/me")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 4) Servicio de hechos dinámico
    @Bean
    public WebClient hechoWebClient() {
        return WebClient.builder()
                .baseUrl(hechosServiceUrl + "/api/hechos")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(cfg -> cfg.defaultCodecs()
                                .maxInMemorySize(32 * 1024 * 1024)) // 32 MB
                        .build())
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip")
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .compress(true)
                                .keepAlive(true)
                ))
                .build();
    }
}
