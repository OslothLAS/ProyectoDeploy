package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.dtos.output.*;
import ar.utn.frba.ddsi.agregador.exceptions.HechoNoEncontradoException;
import ar.utn.frba.ddsi.agregador.exceptions.JustificacionInvalidaException;
import ar.utn.frba.ddsi.agregador.exceptions.SolicitudNoEncontradaException;
import ar.utn.frba.ddsi.agregador.exceptions.UsuarioNoEncontradoException;
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
import ar.utn.frba.ddsi.agregador.utils.HechoUtil;
import ar.utn.frba.ddsi.agregador.utils.SolicitudUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @Autowired
    @Qualifier("hechoWebClient")
    private WebClient hechoWebClient;




    @Transactional
    @Override
    public SolicitudOutputDTO crearSolicitud(SolicitudInputDTO solicitudDTO) {
        // Validar justificación
        String justificacionValida = this.validarJustificacion(solicitudDTO.getJustificacion());


        solicitudDTO.setJustificacion(justificacionValida);

        /*UsuarioDTO usuario = this.obtenerUserPorUsername(solicitudDTO.getUsername());
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("El usuario con username " + solicitudDTO.getUsername() + " no existe.");
        }*/

        HechoOutputDTO hecho = this.obtenerHechoPorId(solicitudDTO.getIdHecho())
                .orElseThrow(() -> new HechoNoEncontradoException(
                        "No se encontró el hecho con id: " + solicitudDTO.getIdHecho()
                ));


        // Convertir DTO -> entidad
        SolicitudEliminacion solicitudNueva = SolicitudUtil.dtoToSolicitud(solicitudDTO);

        // Guardar en BD
        SolicitudEliminacion solicitudPersistida = this.solicitudRepository.save(solicitudNueva);

        // Convertir a DTO de salida
        return SolicitudUtil.solicitudToDTO(solicitudPersistida);
    }

    public UsuarioDTO obtenerUserPorUsername(String username) {
        return usuarioWebClient.get()
                .uri("/username/{username}", username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new UsuarioNoEncontradoException(
                                "Usuario no encontrado con username: " + username)))
                .bodyToMono(UsuarioDTO.class)
                .block();
    }

    public Optional<HechoOutputDTO> obtenerHechoPorId(Long idHecho) {
        return hechoRepository.findById(idHecho)
                .map(HechoUtil::hechoToDTO); // ✅ method reference
    }



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
    public List<SolicitudOutputDTO> getSolicitudesPorUsuario(String solicitante) {
        List<SolicitudEliminacion> solicitudes = this.solicitudRepository.findBySolicitante(solicitante);
        List<SolicitudOutputDTO> solicitudesDTO = solicitudes.stream().map(s -> solicitudToDTO(s)).toList();
        return solicitudesDTO;
    }



    @Override
    public String validarJustificacion(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new JustificacionInvalidaException("La justificación debe tener al menos 500 caracteres");
        }
        return justificacionSolicitud;
    }


    private void cambiarEstadoHecho(SolicitudEliminacion solicitud, String usernameAdmin, PosibleEstadoSolicitud estado, HechoOutputDTO hecho) {
        String username = usernameAdmin;

        EstadoSolicitud estadoSolicitud = new EstadoSolicitud(username,estado);

        if(estado == PosibleEstadoSolicitud.RECHAZADA) {
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);
        }
        else if(estado == PosibleEstadoSolicitud.ACEPTADA){
            solicitud.cambiarEstadoSolicitud(estadoSolicitud);



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

        SolicitudEliminacion solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new SolicitudNoEncontradaException("Solicitud no encontrada con ID: " + idSolicitud));
        HechoOutputDTO hecho = this.obtenerHechoPorId(solicitud.getIdHecho()).orElseThrow(() -> new HechoNoEncontradoException(
                "No se encontró el hecho con id: " + solicitud.getIdHecho()
        ));

        this.cambiarEstadoHecho(solicitud,obtenerUsernamePorSesion(), PosibleEstadoSolicitud.ACEPTADA, hecho);


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
        HechoOutputDTO hecho = this.obtenerHechoPorId(solicitud.getIdHecho()).orElseThrow(() -> new HechoNoEncontradoException(
                "No se encontró el hecho con id: " + solicitud.getIdHecho()
        ));
        this.cambiarEstadoHecho(solicitud,obtenerUsernamePorSesion(), PosibleEstadoSolicitud.RECHAZADA, hecho);
        solicitudRepository.save(solicitud);
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