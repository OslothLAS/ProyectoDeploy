package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.PosibleEstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.TipoUsuario;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IUsuarioRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import jakarta.transaction.Transactional;
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
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Transactional
    @Override
    public Long crearSolicitud(SolicitudInputDTO solicitud) {
        String s = this.validarJustificacion(solicitud.getJustificacion());
        solicitud.setJustificacion(s);
        this.usuarioRepository.findById(solicitud.getIdSolicitante()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        SolicitudEliminacion solicitudEliminacion = this.solicitudRepository.save(this.dtoToSolicitud(solicitud));
        return solicitudEliminacion.getId();
    }

    private SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        Hecho hecho = this.hechoRepository.findById(solicitud.getIdHecho())
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + solicitud.getIdHecho()));

        Usuario usuario = this.usuarioRepository.findById(solicitud.getIdSolicitante()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                hecho,
                usuario);
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
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 10) {
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
                int updated = this.hechoRepository.invalidateByTituloAndDescripcion(hecho.getTitulo(), hecho.getDescripcion());
                System.out.println("modificadas " + updated + " filas");
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
        this.usuarioRepository.save(administrador);
        this.cambiarEstadoHecho(solicitud,administrador, PosibleEstadoSolicitud.ACEPTADA);

        Hecho hecho = solicitud.getHecho();

        List<Fuente> fuentesUnicas = coleccionRepository.findAll().stream()
                .flatMap(coleccion -> coleccion.getImportadores().stream())
                .distinct()
                .toList();

        for (Fuente fuente : fuentesUnicas) {
            fuente.invalidarHecho(hecho.getTitulo(),hecho.getDescripcion());
        }
    }

    @Override
    public void rechazarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idSolicitud));

        Usuario administrador = new Usuario("el", "admin", LocalDate.now(), TipoUsuario.ADMIN);

        this.cambiarEstadoHecho(solicitud,administrador, PosibleEstadoSolicitud.RECHAZADA);
    }

    public StatDTO getCantidadSpam(){
        Long cantSpam = this.solicitudRepository.countSolicitudesSpam();
        return new StatDTO("rachazo","Solicitudes rechazadas por spam",cantSpam);
    }
}