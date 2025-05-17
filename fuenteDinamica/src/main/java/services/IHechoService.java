package services;

import models.entities.hechos.Hecho;
import models.entities.usuarios.Usuario;

public interface IHechoService {
    void crearHecho(Hecho hecho);
    void editarHecho(Hecho hecho);
}
