package ar.utn.frba.ddsi.services.impl;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.models.entities.AgregadorConnector;
import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.models.repositories.IStatRepository;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {
    private final IStatRepository statRepository;

    public EstadisticaService(IStatRepository statRepository) {
        this.statRepository = statRepository;

    }

    public void calcularEstadisticas() {
        List<StatDTO> stats = new ArrayList<>();
        AgregadorConnector agregadorConnector = new AgregadorConnector();

        // Agrego todas las listas
        stats.addAll(agregadorConnector.getCategoriaPorHechos());
        stats.addAll(agregadorConnector.getHechosDeColeccion(1L));
        stats.addAll(agregadorConnector.getProviniciaCategoriaReportada());
        stats.addAll(agregadorConnector.getHoraMasReportada());

        // Agrego el StatDTO individual
        StatDTO spam = agregadorConnector.getSpam();
        if (spam != null) {
            stats.add(spam);
        }

        List<Estadistica> estadisticas = stats.stream()
                .map(Estadistica::fromDTO)
                .collect(Collectors.toList());

        this.statRepository.saveAll(estadisticas);
    }

    public List<StatDTO> generateCSV() {
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public List<StatDTO> calcularProvinciaPorHechos(Long idColeccion){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularCategoriaPorHechos(){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularMaxHechos(Long idCategoria){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularHoraPico(Long idCategoria){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularSolicitudesPorSpam(){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(StatDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
