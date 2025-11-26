package ar.utn.frba.ddsi.agregador.config;

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


    @Bean
    public WebClient usuarioPorSolicitudWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8040/api/users/username") // URL base del servicio de usuarios
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8040/api/users") // URL base del servicio de usuarios
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Bean
    public WebClient userNameWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8040/api/users/me") // URL base del servicio de usuarios
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient hechoWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8070/api/hechos")
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
