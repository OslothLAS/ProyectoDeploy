package com.frontMetaMapa.frontMetaMapa.services;

import com.frontMetaMapa.frontMetaMapa.exceptions.NotFoundException;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.input.SolicitudInputDTO;
import com.frontMetaMapa.frontMetaMapa.models.DTOS.output.SolicitudOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudEliminacionService {

    @Autowired
    private SolicitudesEliminacionApiService solicitudesEliminacionApiService;

    /**
     * Crea una nueva solicitud de eliminación.
     */
    public SolicitudOutputDTO crearSolicitud(SolicitudInputDTO solicitudDTO) {
        validarDatosBasicos(solicitudDTO);
        return solicitudesEliminacionApiService.createSolicitud(solicitudDTO);
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
    public SolicitudOutputDTO aceptarSolicitud(String id) {
        return solicitudesEliminacionApiService.cambiarEstadoSolicitud(id, "aceptar");
    }

    /**
     * Rechaza una solicitud de eliminación.
     */
    public SolicitudOutputDTO rechazarSolicitud(String id) {
        return solicitudesEliminacionApiService.cambiarEstadoSolicitud(id, "rechazar");
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
}
