package models.entities.fuentes;

import models.entities.hechos.Hecho;

import java.util.List;

public class ImportadorHechos implements Importador{
    @Override
    public List<Hecho> obtenerHechos() {
        return List.of();
    }
}
