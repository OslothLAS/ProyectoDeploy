package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import entities.solicitudes.SolicitudEliminacion;

public class SolicitudUtil {
    public static SolicitudOutputDTO solicitudToDTO(SolicitudEliminacion solicitud) {
        SolicitudOutputDTO solicitudOutputDTO = new SolicitudOutputDTO();
        solicitudOutputDTO.setId(solicitud.getId());
        solicitudOutputDTO.setSolicitante(solicitud.getSolicitante());
        solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
        solicitudOutputDTO.setFechaDeEvaluacion(solicitud.getFechaDeEvaluacion());
        solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
        solicitudOutputDTO.setEstados(solicitud.getEstados());

        return solicitudOutputDTO;
    }
}
