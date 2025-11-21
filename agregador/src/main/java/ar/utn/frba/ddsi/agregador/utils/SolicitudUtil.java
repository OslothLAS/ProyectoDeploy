package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.EstadoSolicitudDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SolicitudUtil {
    @Autowired
    @Qualifier("usuarioWebClient")
    private WebClient usuarioWebClient;

    @Autowired
    @Qualifier("usuarioPorSolicitudWebClient")
    private WebClient usuarioSesionWebClient;
    public static SolicitudOutputDTO solicitudToDTO(SolicitudEliminacion solicitud) {
        SolicitudOutputDTO dto = new SolicitudOutputDTO();
        dto.setId(solicitud.getId());
        dto.setUsername(solicitud.getSolicitante());
        dto.setJustificacion(solicitud.getJustificacion());
        dto.setIdHecho(solicitud.getIdHecho());
        dto.setFechaDeCreacion(solicitud.getFechaDeCreacion());
        dto.setEstados(mapEstados(solicitud.getEstados()));

        // Fecha de evaluación = fecha de cambio del último estado, si existe
        if (!solicitud.getEstados().isEmpty()) {
            dto.setFechaDeEvaluacion(
                    solicitud.getEstados().get(solicitud.getEstados().size() - 1).getFechaDeCambio()
            );
        }

        return dto;
    }

    // Convertir DTO de entrada a entidad
    public static SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                solicitud.getIdHecho(),
                (solicitud.getUsername() != null && !solicitud.getUsername().isBlank())
                        ? solicitud.getUsername()
                        : null
        );
    }


    // Mapear lista de estados a DTOs
    public static List<EstadoSolicitudDTO> mapEstados(List<EstadoSolicitud> estados) {
        if (estados == null) return new ArrayList<>();
        return estados.stream()
                .map(EstadoSolicitudDTO::new)
                .collect(Collectors.toList());
    }

    /*
    public static Usuario obtenerPorUsername(String username) {
        return usuarioSesionWebClient.get()
                .uri("/{username}", username)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.NOT_FOUND),
                        error -> Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(Usuario.class)
                .block();
    }*/



}
