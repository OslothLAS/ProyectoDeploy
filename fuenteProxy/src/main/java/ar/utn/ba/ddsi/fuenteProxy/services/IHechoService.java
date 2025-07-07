package ar.utn.ba.ddsi.fuenteProxy.services;

import entities.colecciones.Handle;
import entities.hechos.Hecho;

import java.util.List;

public interface IHechoService {
    List<Hecho> getHechos();
    List<Hecho> getHechosXcoleccionXmetamapa(Handle id_coleccion, Long metamapa);
    List<Hecho> getHechosXcategoriaXcolecciones(String categoria, Long metamapa, Handle id_coleccion);
    List<Hecho> getHechosXmetamapa(Long id);
    List<Hecho> getHechosXColeccionXmetampaXModoNavegacion(String modoNavegacion, Handle id_coleccion, Long metamapa);
}
