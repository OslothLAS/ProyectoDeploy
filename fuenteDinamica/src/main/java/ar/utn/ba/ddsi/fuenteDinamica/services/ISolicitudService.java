package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.SolicitudInputDTO;

public interface ISolicitudService {
    void createSolicitud(SolicitudInputDTO solicitud);
}
