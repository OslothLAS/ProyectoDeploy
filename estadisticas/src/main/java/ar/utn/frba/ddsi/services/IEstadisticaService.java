package ar.utn.frba.ddsi.services;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import java.util.List;

public interface IEstadisticaService {
    List<Estadistica> calcularHechosPorProvincia(Long idColeccion);
}
