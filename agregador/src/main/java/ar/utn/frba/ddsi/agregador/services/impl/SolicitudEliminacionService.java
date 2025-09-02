package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import entities.colecciones.Fuente;
import entities.hechos.Hecho;
import entities.solicitudes.EstadoSolicitud;
import entities.solicitudes.PosibleEstadoSolicitud;
import entities.solicitudes.SolicitudEliminacion;
import entities.usuarios.TipoUsuario;
import entities.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    @Autowired
    private ISolicitudEliminacionRepository solicitudRepository;
    @Autowired
    private IHechoRepository hechoRepository;
    @Autowired
    private IColeccionRepository coleccionRepository;

    @Override
    public Long crearSolicitud(SolicitudInputDTO solicitud) {
        String s = this.validarJustificacion(solicitud.getJustificacion());
        solicitud.setJustificacion(s);
        SolicitudEliminacion solicitudEliminacion =this.solicitudRepository.save(this.dtoToSolicitud(solicitud));
        return solicitudEliminacion.getId();
    }

    private SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        Hecho hecho = this.hechoRepository.findById(solicitud.getIdHecho());
        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                hecho,
                solicitud.getSolicitante());
    }

    private Hecho obtenerHechoconTituloyDesc(List<String> tituloYdesc) {
        String tituloBuscado = tituloYdesc.get(0);
        String descripcionBuscada = tituloYdesc.get(1);

        return hechoRepository.findAll().stream()
                .filter(hecho -> hecho.getDatosHechos().getTitulo().equals(tituloBuscado) &&
                        hecho.getDatosHechos().getDescripcion().equals(descripcionBuscada))
                .findFirst()
                .orElse(null);
    }



    @Override
    public SolicitudEliminacion getSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository
                .findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con id: " + idSolicitud));
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

    private void cambiarEstadoHecho(SolicitudEliminacion solicitud, Usuario admin, PosibleEstadoSolicitud estado) {
        EstadoSolicitud estadoSolicitud = new EstadoSolicitud(admin,estado);
        if(estado == PosibleEstadoSolicitud.RECHAZADA) {
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);
        }
        else if(estado == PosibleEstadoSolicitud.ACEPTADA){
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);

            Hecho hecho = solicitud.getHecho();

            if (hecho != null) {
                hecho.setEsValido(false);
            } else {
                System.err.println("Hecho no encontrado en agregador");
            }
        }
    }

    @Override
    public void aceptarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + idSolicitud));

        Usuario administrador = new Usuario("el", "admin", LocalDate.now(), TipoUsuario.ADMIN);

        this.cambiarEstadoHecho(solicitud,administrador, PosibleEstadoSolicitud.ACEPTADA);

        Hecho hecho = solicitud.getHecho();

        List<Fuente> fuentesUnicas = coleccionRepository.findAll().stream()
                .flatMap(coleccion -> coleccion.getImportadores().stream())
                .distinct()
                .toList();

        for (Fuente fuente : fuentesUnicas) {
            fuente.invalidarHecho(hecho.getDatosHechos().getTitulo(),hecho.getDatosHechos().getDescripcion());
        }
    }

    @Override
    public void rechazarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idSolicitud));

        Usuario administrador = new Usuario("el", "admin", LocalDate.now(), TipoUsuario.ADMIN);

        this.cambiarEstadoHecho(solicitud,administrador, PosibleEstadoSolicitud.RECHAZADA);
    }


}