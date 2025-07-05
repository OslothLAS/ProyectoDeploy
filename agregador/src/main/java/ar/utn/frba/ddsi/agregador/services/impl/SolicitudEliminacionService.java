package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.ColeccionOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Administrador;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import entities.colecciones.Coleccion;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    private ISolicitudEliminacionRepository solicitudRepository;

    @Override
    public void crearSolicitud(SolicitudInputDTO solicitud) {
        String s = this.validarJustificacion(solicitud.getJustificacion());
        solicitudRepository.save(this.dtoToSolicitud(solicitud));
    }

    private SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                solicitud.getId(),
                solicitud.getSolicitante());
    }

    private SolicitudOutputDTO solicitudToDTO(SolicitudEliminacion solicitud) {
        SolicitudOutputDTO solicitudOutputDTO = new SolicitudOutputDTO();
            solicitudOutputDTO.setId(solicitud.getId());
            solicitudOutputDTO.setSolicitante(solicitud.getSolicitante());
            solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
            solicitudOutputDTO.setFechaDeEvaluacion(solicitud.getFechaDeEvaluacion());
            solicitudOutputDTO.setJustificacion(solicitud.getJustificacion());
            solicitudOutputDTO.setEstado(solicitud.getEstado());
            solicitudOutputDTO.setHistorialDeSolicitud(solicitud.getHistorialDeSolicitud());

            return solicitudOutputDTO;
    }

    @Override
    public SolicitudEliminacion getSolicitud(Long idSolicitud) {
        return solicitudRepository.findById(idSolicitud).orElse(null);
    }

    @Override
    public String validarJustificacion(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            return justificacionSolicitud;
        }
    }

    @Override
    public void aceptarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idSolicitud));

        Administrador administrador = new Administrador(1L, "admin"); //ESTE ADMINISTRADOR DEBERIA VENIR EN PARAMS DE LOGIN

        solicitud.cambiarEstadoHecho(administrador, EstadoSolicitudEliminacion.ACEPTADA);
    }

    @Override
    public void rechazarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idSolicitud));

        Administrador administrador = new Administrador(1L, "admin"); //ESTE ADMINISTRADOR DEBERIA VENIR EN PARAMS DE LOGIN

        solicitud.cambiarEstadoHecho(administrador, EstadoSolicitudEliminacion.RECHAZADA);
    }


}