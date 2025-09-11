package ar.utn.frba.ddsi.services;

import ar.utn.frba.ddsi.dtos.StatDTO;
import java.util.List;

public interface IEstadisticaService {
    void calcularEstadisticas();
    List<StatDTO> calcularProvinciaPorHechos(Long idColeccion);
    StatDTO calcularCategoriaPorHechos();
    List<StatDTO> calcularMaxHechos(Long categoria);
    List<StatDTO> calcularHoraPico(Long categoria);
    List<StatDTO> calcularSolicitudesPorSpam();
    List<StatDTO> generateCSV();
}
