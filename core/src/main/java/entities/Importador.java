package entities;

import entities.hechos.Hecho;

import java.util.List;

public interface Importador {
    List<Hecho> obtenerHechos();
}
