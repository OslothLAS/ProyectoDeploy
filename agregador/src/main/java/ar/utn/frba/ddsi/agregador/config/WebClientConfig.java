package ar.utn.frba.ddsi.agregador.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

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
}
