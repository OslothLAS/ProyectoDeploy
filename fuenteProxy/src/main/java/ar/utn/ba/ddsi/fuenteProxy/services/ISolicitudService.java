package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;

import java.util.List;

public interface ISolicitudService {
    List<SolicitudDto> getSolicitudesXmetamapa(Long metamapaId);
    SolicitudDto postSolicitudesXmetamapa(Long id, SolicitudesInputDto solicitud);
}
