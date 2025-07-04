package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;

import java.util.List;

public interface IColeccionService {
    List<ColeccionDto> getColeccionesXmetamapa(Long metamapaId);

}
