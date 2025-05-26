package services;

import models.entities.colecciones.Coleccion;
import models.repositories.IHechoRepository;

import java.util.List;

public interface IColeccionService {

    public void actualizarHechos(IHechoRepository repositorioDeFuente, List<Coleccion> colecciones);

}
