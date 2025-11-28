package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.RateLimitException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.UserRolesPermissionsDTO;
import com.frontMetaMapa.frontMetaMapa.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class LoginApiService {
    private static final Logger log = LoggerFactory.getLogger(ColeccionesApiService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String authServiceUrl;
    @Autowired
    public LoginApiService(WebApiCallerService webApiCallerService,
                                 @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.authServiceUrl = authServiceUrl;
    }
    public AuthResponseDTO login(String username, String password) {
        try {
            String urlFinal = authServiceUrl.endsWith("/") ? authServiceUrl + "api/auth" : authServiceUrl + "/api/auth";

            AuthResponseDTO response = webClient
                    .post()
                    .uri(urlFinal)
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();

            System.out.println("🎯 === LOGIN EXITOSO ===" + response);
            return response;

        } catch (WebClientResponseException e) {
            log.error(e.getMessage());

            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                String header = e.getHeaders().getFirst("Retry-After");
                long segundos = (header != null) ? Long.parseLong(header) : 60;

                System.out.println("429 DETECTADO. Segundos de espera: " + segundos);

                throw new RateLimitException(segundos);
            }

            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return null;
            }

            // Otros errores HTTP
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);

        } catch (Exception e) {

            if (e instanceof RateLimitException) {
                throw (RateLimitException) e;
            }

            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    public UserRolesPermissionsDTO getRolesPermisos(String accessToken) {
        try {
            UserRolesPermissionsDTO response = webApiCallerService.getWithAuth(
                    authServiceUrl + "/auth/user/roles-permisos",
                    accessToken,
                    UserRolesPermissionsDTO.class
            );
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
        }
    }

}
