package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;

import java.util.Optional;


public class SolicitudUtil {
    public static SolicitudOutputDTO solicitudToDTO(SolicitudEliminacion solicitud) {
        SolicitudOutputDTO solicitudOutputDTO = new SolicitudOutputDTO();
        solicitudOutputDTO.setUsername(solicitud.getSolicitante());
        solicitudOutputDTO.setId(solicitud.getId());
        solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
        solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
        solicitudOutputDTO.setEstados(solicitud.getEstados());

        return solicitudOutputDTO;
    }
}
