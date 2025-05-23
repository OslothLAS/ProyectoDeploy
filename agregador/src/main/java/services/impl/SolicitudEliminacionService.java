package services.impl;

import entities.usuarios.Contribuyente;
import models.entities.solicitudes.EstadoSolicitudEliminacion;
import models.entities.solicitudes.SolicitudEliminacion;
import models.repositories.ISolicitudEliminacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.ISolicitudEliminacionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service

public class SolicitudEliminacionService implements ISolicitudEliminacionService{

    @Autowired
    private final ISolicitudEliminacionRepository solicitudEliminacionRepository;

    public SolicitudEliminacionService(ISolicitudEliminacionRepository solicitudRepository) {
        this.solicitudEliminacionRepository = solicitudRepository;
    }

    @Override
    public void crearSolicitud(String justificacion, Long idHecho, Contribuyente solicitante) {
            SolicitudEliminacion solicitud = new SolicitudEliminacion(
                    justificacion,
                    idHecho,
                    solicitante
            );
    }
}
