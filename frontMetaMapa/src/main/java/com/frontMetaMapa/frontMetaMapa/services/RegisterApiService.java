package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.TipoUsuario;
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
            log.info("🎯 === INICIANDO REGISTRO ===");
            log.info("Usuario recibido: {}", usuarioDTO);

            // Asignar rol por defecto si no viene del formulario
            if (usuarioDTO.getRol() == null) {
                usuarioDTO.setRol(TipoUsuario.CONTRIBUYENTE); // O el enum que uses
                log.info("Rol asignado por defecto: {}", usuarioDTO.getRol());
            }

            Map<String, String> requestBody = Map.of(
                    "username", usuarioDTO.getUsername(),
                    "contrasenia", usuarioDTO.getContrasenia(),
                    "nombre", usuarioDTO.getNombre(),
                    "apellido", usuarioDTO.getApellido(),
                    "fechaNacimiento", usuarioDTO.getFechaNacimiento().toString(),
                    "rol", usuarioDTO.getRol().name()
            );

            log.info("📤 Enviando a: {}", usersServiceUrl + "/api/users");
            log.info("📦 Body: {}", requestBody);

            // Llamada POST
            webClient.post()
                    .uri(usersServiceUrl + "/api/users")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            log.info("✅ Usuario registrado exitosamente");
            return true;

        } catch (WebClientResponseException e) {
            log.error("❌ Error HTTP: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                return false;
            }
            throw new RuntimeException("Error en servicio de usuarios: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("💥 Excepción: {}", e.getMessage(), e);
            throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
        }
    }
}
