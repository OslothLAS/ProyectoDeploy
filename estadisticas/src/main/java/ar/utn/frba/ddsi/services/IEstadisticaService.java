package ar.utn.frba.ddsi.services;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import java.util.List;

public interface IEstadisticaService {
    List<Estadistica> calcularProvinciaPorHechos(Long idColeccion);
    List<Estadistica> calcularCategoriaPorHechos();
    List<Estadistica> calcularMaxHechos(Long categoria);
    List<Estadistica> calcularHoraPico(Long categoria);
    List<Estadistica> calcularSolicitudesPorSpam();
}
