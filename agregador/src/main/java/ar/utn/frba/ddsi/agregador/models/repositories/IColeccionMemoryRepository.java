package ar.utn.frba.ddsi.agregador.models.repositories;

import entities.colecciones.Coleccion;

import java.util.List;

public interface IColeccionMemoryRepository {


    public List<Coleccion> getColecciones();

    public void actualizarColecciones(List<Coleccion> listaColecciones);
}
