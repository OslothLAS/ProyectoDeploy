package ar.utn.frba.ddsi.services;

import ar.utn.frba.ddsi.dtos.StatDTO;
import java.util.List;

public interface IEstadisticaService {
    void calcularEstadisticas();
    StatDTO getProvinciaMasReportadaPorColeccion(String nombreColeccion);
    StatDTO getCategoriaConMasHechos();
    StatDTO getProvinciaConMasHechosDeCategoria(String categoria);
    StatDTO getHoraPicoDeCategoria(String categoria);
    StatDTO getCantidadDeSpam();
    List<StatDTO> generateCSV();
    List<StatDTO> findAll();
}
