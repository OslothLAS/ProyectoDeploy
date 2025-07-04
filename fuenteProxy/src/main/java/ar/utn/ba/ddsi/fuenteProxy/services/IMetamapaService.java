package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import entities.colecciones.Handle;
import entities.hechos.Hecho;

import java.util.List;

public interface IMetamapaService {
    List<Hecho> getHechos();
    List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa);
    List<Hecho> getHechosXcategoriaXcolecciones(String categoria, Long metamapa, Handle id_coleccion);
    List<Hecho> getHechosXmetamapa(Long id);
    List<Hecho> getHechosXColeccionXmetampaXModoNavegacion(String modoNavegacion, Handle id_coleccion, Long metamapa);
    List<SolicitudDto> getSolicitudesXmetamapa(Long metamapaId);
    List<ColeccionDto> getColeccionesXmetamapa(Long metamapaId);
    SolicitudDto postSolicitudesXmetamapa(Long id, SolicitudesInputDto solicitud);
}
