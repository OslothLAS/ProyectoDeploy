package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.ColeccionInputDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudDto;
import ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud.SolicitudesInputDto;
import entities.colecciones.Handle;
import entities.hechos.Hecho;

import java.util.List;

public interface IMetamapaAdminService {
    List<Hecho> getHechos();
    List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa);
    List<Hecho> getHechosXcategoria(String categoria);
    List<Hecho> getHechosXmetamapa(Long id);
    List<Hecho> getHechosXColeccionXmetampaXModoNavegacion(String modoNavegacion, Handle id_coleccion, Long metamapa);
    List<SolicitudDto> getSolicitudesXmetamapa(Long metamapaId);
    List<ColeccionDto> getColeccionesXmetamapa(Long metamapaId);
    SolicitudDto postSolicitudesXmetamapa(Long id, SolicitudesInputDto solicitud);

    ColeccionDto postColeccionesXmetamapa(Long metamapa, ColeccionInputDto inputDto);

    ColeccionDto putColeccionesXmetamapa(Long metamapa, Long id_coleccion, ColeccionInputDto inputColeccion);

    void eliminarColeccionesXmetamapa(Long metamapa, Long id_coleccion);

    SolicitudDto putSolicitudesXmetamapa(Long metamapa, Long id_solicitud, String estado);
}