package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.EstadoSolicitud;
import com.frontMetaMapa.frontMetaMapa.models.dtos.Api.SolicitudApiOutputDto;
import com.frontMetaMapa.frontMetaMapa.models.dtos.input.SolicitudInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.dtos.output.SolicitudOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudEliminacionService {

    @Autowired
    private SolicitudesEliminacionApiService solicitudesEliminacionApiService;

    /**
     * Crea una nueva solicitud de eliminación.
     */
    public void crearSolicitud(SolicitudInputDTO solicitudDTO) {
        validarDatosBasicos(solicitudDTO);
        solicitudesEliminacionApiService.createSolicitud(solicitudDTO);
    }

    /**
     * Obtiene una solicitud por su ID.
     */
    public SolicitudOutputDTO obtenerSolicitudPorId(String id) {
        try {
            return solicitudesEliminacionApiService.obtenerSolicitudPorId(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Solicitud", id);
        }
    }

    /**
     * Acepta una solicitud de eliminación.
     */
    public void aceptarSolicitud(String id) {
         solicitudesEliminacionApiService.cambiarEstadoSolicitud(id, "aceptar");
    }

    public List<SolicitudOutputDTO> obtenerSolicitudes() {
        List<SolicitudApiOutputDto> solicitudesApi = solicitudesEliminacionApiService.obtenerSolicitudes();
        return convertirASolicitudOutputDTO(solicitudesApi);
    }

    /**
     * Rechaza una solicitud de eliminación.
     */
    public void rechazarSolicitud(String id) {
         solicitudesEliminacionApiService.cambiarEstadoSolicitud(id, "rechazar");
    }

    /**
     * Valida los datos obligatorios antes de crear la solicitud.
     */
    private void validarDatosBasicos(SolicitudInputDTO solicitudDTO) {
        if (solicitudDTO == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        if (solicitudDTO.getIdHecho() == null) {
            throw new IllegalArgumentException("El id del hecho es obligatorio");
        }

        if (solicitudDTO.getIdSolicitante() == null) {
            throw new IllegalArgumentException("El id del solicitante es obligatorio");
        }

        if (solicitudDTO.getJustificacion() == null || solicitudDTO.getJustificacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La justificación es obligatoria");
        }
    }


    public List<SolicitudOutputDTO> convertirASolicitudOutputDTO(List<SolicitudApiOutputDto> solicitudesApi) {
        return solicitudesApi.stream().map(solicitudApi -> {
            SolicitudOutputDTO dto = new SolicitudOutputDTO();
            dto.setId(solicitudApi.getId());
            dto.setUsername(solicitudApi.getUsername());
            dto.setFechaDeCreacion(solicitudApi.getFechaDeCreacion());
            dto.setFechaDeEvaluacion(solicitudApi.getFechaDeEvaluacion());
            dto.setJustificacion(solicitudApi.getJustificacion());
            dto.setIdHecho(solicitudApi.getIdHecho());

            // Tomamos el último estado de la lista
            if (solicitudApi.getEstados() != null && !solicitudApi.getEstados().isEmpty()) {
                EstadoSolicitud ultimoEstado = solicitudApi.getEstados()
                        .get(solicitudApi.getEstados().size() - 1);
                dto.setEstado(ultimoEstado.getEstado().name()); // convertir enum a String
                dto.setEvaluador(ultimoEstado.getEvaluador());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
