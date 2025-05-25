package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios.Contribuyente;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.ISolicitudRepository;
import ar.utn.ba.ddsi.fuenteDinamica.services.ISolicitudService;
import config.HechoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolicitudService implements ISolicitudService {
    private final IHechoRepository hechoRepository;
    private final ISolicitudRepository solicitudRepository;

    //mejorar pasandolo por properties
    private static final int MIN_CARACTERES_JUSTIFICACION = 500;

    public void createSolicitud(SolicitudInputDTO solicitud) {
        // Validación de entrada
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        validarJustificacion(solicitud.getJustificacion());

        Hecho hecho = hechoRepository.findById(solicitud.getIdHecho())
                .orElseThrow(() -> new IllegalArgumentException("No existe el hecho con ID: " + solicitud.getIdHecho()));

        /* Pendiente implementación con contribuyente
        Contribuyente contribuyente = contribuyenteRepository.findById(solicitud.getIdContribuyente())
            .orElseThrow(() -> new IllegalArgumentException("No existe el contribuyente con ID: " + solicitud.getIdContribuyente()));
        */

        // Crear y guardar la solicitud
        SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(
                solicitud.getJustificacion(),
                solicitud.getIdHecho(),
                solicitud.getIdContribuyente() // Reemplazar con contribuyente cuando se implemente
        );

        nuevaSolicitud.setHecho(hecho);
        //nuevaSolicitud.setContribuyente(contribuyente); Reemplazar con contribuyente cuando se implemente

        solicitudRepository.save(nuevaSolicitud);
    }

    private void validarJustificacion(String justificacion) {
        if (justificacion == null || justificacion.trim().length() < MIN_CARACTERES_JUSTIFICACION) {
            throw new IllegalArgumentException(
                    String.format("La justificación debe tener al menos %d caracteres", MIN_CARACTERES_JUSTIFICACION)
            );
        }
    }
}
