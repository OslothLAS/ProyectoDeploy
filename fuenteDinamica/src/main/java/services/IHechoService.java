package services;

import models.entities.hechos.Hecho;

public interface IHechoService {
    void crearHecho(Hecho hecho);
    void editarHecho(Hecho hecho);
}
