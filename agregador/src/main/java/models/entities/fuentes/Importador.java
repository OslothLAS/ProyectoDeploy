package models.entities.fuentes;

import models.entities.hechos.Hecho;

import java.util.List;

public interface Importador {
    List<Hecho> obtenerHechos();
}
