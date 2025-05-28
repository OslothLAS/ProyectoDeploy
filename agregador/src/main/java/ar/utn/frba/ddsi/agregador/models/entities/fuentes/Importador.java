package ar.utn.frba.ddsi.agregador.models.entities.fuentes;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public interface Importador {
    List<Hecho> obtenerHechos();
}
