package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.models.dtos.input.UsuarioDTO;
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
public class RegisterApiService {
    private static final Logger log = LoggerFactory.getLogger(RegisterApiService.class);

    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String usersServiceUrl;

    @Autowired
    public RegisterApiService(WebApiCallerService webApiCallerService,
                              @Value("${users.service.url}") String usersServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.usersServiceUrl = usersServiceUrl;
    }

    public boolean registerUser(UsuarioDTO usuarioDTO) {
        try {
            log.info("Registrando usuario: {}", usuarioDTO);
            Map<String, String> requestBody = Map.of(
                    "username", usuarioDTO.getUsername(),
                    "contrasenia", usuarioDTO.getContrasenia(),
                    "nombre", usuarioDTO.getNombre(),
                    "apellido", usuarioDTO.getApellido(),
                    "fechaNacimiento", usuarioDTO.getFechaNacimiento().toString(), // LocalDate a String
                    "rol", usuarioDTO.getRol().name() // Enum a String
            );

            log.info("=== DETALLES DE LA PETICIÓN ===");
            log.info("URL destino: {}", usersServiceUrl + "/api/users");
            log.info("Request Body: {}", requestBody);
            log.info("Headers: Content-Type: application/json");
            log.info("================================");

            // Llamada POST al microservicio de usuarios
            webClient.post()
                    .uri(usersServiceUrl + "/api/users")
                    .bodyValue(requestBody)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("Usuario registrado correctamente: {}", usuarioDTO.getUsername());
            return true;

        } catch (WebClientResponseException e) {
            log.error("Error al registrar usuario: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                // Usuario ya existe
                return false;
            }
            throw new RuntimeException("Error en el servicio de usuarios: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Excepción al registrar usuario: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio de usuarios: " + e.getMessage(), e);
        }
    }
}
