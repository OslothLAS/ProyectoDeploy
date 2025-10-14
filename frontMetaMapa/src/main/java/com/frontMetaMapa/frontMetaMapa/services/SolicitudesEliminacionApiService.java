package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.SolicitudInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.AuthResponseDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.ColeccionOutputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.UserRolesPermissionsDTO;

import com.frontMetaMapa.frontMetaMapa.models.dtos.output.SolicitudOutputDTO;
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
public class SolicitudesEliminacionApiService {
    private static final Logger log = LoggerFactory.getLogger(SolicitudesEliminacionApiService.class);
    private final WebClient webClient;
    private final WebApiCallerService webApiCallerService;
    private final String authServiceUrl;
    private final String solicitudesServiceUrl;

    @Autowired
    public SolicitudesEliminacionApiService(WebApiCallerService webApiCallerService,
                                            @Value("${auth.service.url}") String authServiceUrl,
                                            @Value("${solicitudes.service.url}")String solicitudesServiceUrl) {
        this.webClient = WebClient.builder().build();
        this.webApiCallerService = webApiCallerService;
        this.authServiceUrl = authServiceUrl;
        this.solicitudesServiceUrl = solicitudesServiceUrl;
    }
    public AuthResponseDTO login(String username, String password) {
        try {
            AuthResponseDTO response = webClient
                    .post()
                    .uri(authServiceUrl + "/auth")
                    .bodyValue(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Login fallido - credenciales incorrectas
                return null;
            }
            // Otros errores HTTP
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
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
    public SolicitudOutputDTO createSolicitud(SolicitudInputDTO solicitudDTO){
        SolicitudOutputDTO response = webApiCallerService.post(solicitudesServiceUrl + "/solicitudes", solicitudDTO, SolicitudOutputDTO.class);
        if (response == null) {
            throw new RuntimeException("Error al crear solicitud en el servicio externo");
        }
        return response;
    }

    public SolicitudOutputDTO obtenerSolicitudPorId(String id) {
        SolicitudOutputDTO response = webApiCallerService.get(solicitudesServiceUrl + "/solicitudes/" + id, SolicitudOutputDTO.class);
        if (response == null) {
            throw new NotFoundException("Solicitud", id);
        }
        return response;
    }

    public SolicitudOutputDTO cambiarEstadoSolicitud(String id, String accion) {
        SolicitudOutputDTO response = webApiCallerService.post(
                solicitudesServiceUrl + "/solicitudes/" + id + "/" + accion,
                null,
                SolicitudOutputDTO.class
        );

        if (response == null) {
            throw new NotFoundException("Solicitud", id);
        }

        return response;
    }





}
