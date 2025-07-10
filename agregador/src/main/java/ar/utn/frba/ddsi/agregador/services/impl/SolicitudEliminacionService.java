package ar.utn.frba.ddsi.agregador.services.impl;

import ar.utn.frba.ddsi.agregador.dtos.input.SolicitudInputDTO;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Administrador;
import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.repositories.IColeccionRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.IHechoRepository;
import ar.utn.frba.ddsi.agregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.frba.ddsi.agregador.services.ISolicitudEliminacionService;
import entities.colecciones.Fuente;
import entities.hechos.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Long crearSolicitud(SolicitudInputDTO solicitud) {
        String s = this.validarJustificacion(solicitud.getJustificacion());
        solicitud.setJustificacion(s);
        return solicitudRepository.save(this.dtoToSolicitud(solicitud));
    }

    private SolicitudEliminacion dtoToSolicitud(SolicitudInputDTO solicitud){
        Long hechoID = obtenerIDconTituloyDesc(List.of(solicitud.getTitulo(),solicitud.getDescripcion()));
        return new SolicitudEliminacion(
                solicitud.getJustificacion(),
                hechoID,
                solicitud.getSolicitante());
    }

    private Long obtenerIDconTituloyDesc(List<String> tituloYdesc) {
        String tituloBuscado = tituloYdesc.get(0);
        String descripcionBuscada = tituloYdesc.get(1);

        return hechoRepository.findAll().stream()
                .filter(hecho -> hecho.getDatosHechos().getTitulo().equals(tituloBuscado) &&
                        hecho.getDatosHechos().getDescripcion().equals(descripcionBuscada))
                .map(Hecho::getId)
                .findFirst()
                .orElse(null);
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

    private void cambiarEstadoHecho(SolicitudEliminacion solicitud, Administrador admin, EstadoSolicitudEliminacion estado) {
        if(estado == EstadoSolicitudEliminacion.RECHAZADA) {
            solicitud.cambiarEstadoSolicitud(estado);
        }
        else if(estado == EstadoSolicitudEliminacion.ACEPTADA){
            solicitud.cambiarEstadoSolicitud(estado);

            Hecho hecho = hechoRepository.findById(solicitud.getIdHecho());

            if (hecho != null) {
                hecho.setEsValido(false);
            } else {
                System.err.println("Hecho no encontrado en agregador");
            }

        }
        solicitud.actualizarHistorialDeOperacion(estado, admin);
    }

    @Override
    public void aceptarSolicitud(Long idSolicitud) {
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + idSolicitud));

        Administrador administrador = new Administrador(1L, "admin");

        this.cambiarEstadoHecho(solicitud,administrador, EstadoSolicitudEliminacion.ACEPTADA);

        Hecho hecho = hechoRepository.findById(solicitud.getIdHecho());

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

        Administrador administrador = new Administrador(1L, "admin"); //ESTE ADMINISTRADOR DEBERIA VENIR EN PARAMS DE LOGIN

        this.cambiarEstadoHecho(solicitud,administrador, EstadoSolicitudEliminacion.RECHAZADA);
    }


}