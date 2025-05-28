package ar.utn.frba.ddsi.agregador.models.entities.fuentes;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;

import java.util.List;

public class ImportadorHechos implements Importador{
    @Override
    public List<Hecho> obtenerHechos() {
        return List.of();
    }
}
