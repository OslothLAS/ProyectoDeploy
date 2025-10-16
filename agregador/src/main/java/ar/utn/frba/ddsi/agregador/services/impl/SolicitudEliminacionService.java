package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.DescripcionStat;
import ar.utn.frba.ddsi.agregador.dtos.output.SolicitudOutputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.StatDTO;
import ar.utn.frba.ddsi.agregador.models.entities.colecciones.Fuente;
import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.PosibleEstadoSolicitud;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Usuario;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static ar.utn.frba.ddsi.agregador.utils.SolicitudUtil.solicitudToDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {

    @Autowired
    private ISolicitudEliminacionRepository solicitudRepository;
    @Autowired
    private IHechoRepository hechoRepository;
    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    @Qualifier("usuarioWebClient")
    private WebClient usuarioWebClient;

    @Autowired
    @Qualifier("usuarioPorSolicitudWebClient")
    private WebClient usuarioSesionWebClient;


    public Usuario obtenerPorUsername(String username) {
        return usuarioSesionWebClient.get()
                .uri("/{username}", username)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.NOT_FOUND),
                        error -> Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(Usuario.class)
                .block();
    }

    @Transactional
    @Override
    public Long crearSolicitud(SolicitudInputDTO solicitud) {
        String s = this.validarJustificacion(solicitud.getJustificacion());
        solicitud.setJustificacion(s);
        this.obtenerPorUsername(solicitud.getUsername());
        SolicitudEliminacion solicitudEliminacion = this.solicitudRepository.save(this.dtoToSolicitud(solicitud));
        return solicitudEliminacion.getId();
    }


/*
    public Usuario obtenerUserPorId(Long id) {
        return usuarioWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.NOT_FOUND),
                        error -> Mono.error(new RuntimeException("Usuario no encontrado")))
                .bodyToMono(Usuario.class)
                .block();
    }*/

    public String obtenerUsernamePorSesion() {
        // 1. Obtener el objeto de autenticación del contexto.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Validar que la autenticación sea válida.
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No hay un usuario autenticado en la sesión actual.");
        }

        // 3. Obtener el USERNAME del Principal.
        //    El método .getName() devuelve el identificador del usuario (generalmente el username).
        String username = authentication.getName();
        if (username == null) {
            throw new IllegalStateException("El Principal de la autenticación no tiene un nombre de usuario.");
        }

        return username;

    }

    private SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        Hecho hecho = hechoRepository.findById(solicitud.getIdHecho()).orElse(null);
        Usuario usuario = this.obtenerPorUsername(solicitud.getUsername());

        System.out.println("USUARIO ENCONTRADO POR API: " + usuario.getUsername());

        assert hecho != null;
        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                hecho.getId(),
                usuario.getUsername());
    }

    @Override
    public SolicitudEliminacion getSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository
                .findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con id: " + idSolicitud));
        return solicitudRepository.findById(idSolicitud).orElse(null);
    }

    public List<SolicitudOutputDTO> getSolicitudes() {
        List<SolicitudEliminacion> solicitudes = this.solicitudRepository.findAll();
        List<SolicitudOutputDTO> solicitudesDTO = solicitudes.stream().map(s -> solicitudToDTO(s)).toList();
        return solicitudesDTO;
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

    private void cambiarEstadoHecho(SolicitudEliminacion solicitud, String usernameAdmin, PosibleEstadoSolicitud estado) {
        String username = usernameAdmin;

        EstadoSolicitud estadoSolicitud = new EstadoSolicitud(username,estado);

        if(estado == PosibleEstadoSolicitud.RECHAZADA) {
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);
        }
        else if(estado == PosibleEstadoSolicitud.ACEPTADA){
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);

            Hecho hecho = hechoRepository.findById(solicitud.getId()).orElse(null);

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


        this.cambiarEstadoHecho(solicitud,obtenerUsernamePorSesion(), PosibleEstadoSolicitud.ACEPTADA);

        Hecho hecho = hechoRepository.findById(solicitud.getId()).orElse(null);

        List<Fuente> fuentesUnicas = coleccionRepository.findAll().stream()
                .flatMap(coleccion -> coleccion.getImportadores().stream())
                .distinct()
                .toList();

        this.invalidarHechoAgregador(hecho.getTitulo(),hecho.getDescripcion());
        for (Fuente fuente : fuentesUnicas) {
            fuente.invalidarHecho(hecho.getTitulo(),hecho.getDescripcion());
        }

    }
    @Override
    public void rechazarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada con ID: " + idSolicitud));

        this.cambiarEstadoHecho(solicitud,obtenerUsernamePorSesion(), PosibleEstadoSolicitud.RECHAZADA);
    }


    private void invalidarHechoAgregador(String titulo, String descripcion) {
        Optional<Hecho> hechoInvalido = hechoRepository.findByTituloAndDescripcion(titulo, descripcion);
        hechoInvalido.ifPresent(hecho -> {
            hecho.setEsValido(false);
            hechoRepository.save(hecho);
        });
    }

   public StatDTO getCantidadSpam(){
        Long cantSpam = this.solicitudRepository.countSolicitudesSpam();
        return new StatDTO("","rachazo","Solicitudes rechazadas por spam",1, DescripcionStat.solicitudes_spam,cantSpam, LocalDateTime.now());
    }
}