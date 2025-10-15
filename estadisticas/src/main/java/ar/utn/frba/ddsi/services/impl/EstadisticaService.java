package ar.utn.frba.ddsi.services.impl;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.models.entities.AgregadorConnector;
import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.models.repositories.IStatRepository;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {
    private final IStatRepository statRepository;

    public EstadisticaService(IStatRepository statRepository) {
        this.statRepository = statRepository;

    }

    public Estadistica fromDTO(StatDTO dto) {
        Estadistica e = new Estadistica();
        e.setTituloColeccion(dto.getTituloColeccion());
        e.setProvincia(dto.getProvincia());
        e.setCategoria(dto.getCategoria());
        e.setHora(dto.getHora());
        e.setDescripcion(dto.getDescripcion());
        e.setCantidad(dto.getCantidad());
        e.setFechaStat(dto.getFechaStat() != null ? dto.getFechaStat() : LocalDateTime.now());
        return e;
    }

    public StatDTO toDTO(Estadistica stat) {
        return new StatDTO(
                stat.getTituloColeccion(),
                stat.getProvincia(),
                stat.getCategoria(),
                stat.getHora(),
                stat.getDescripcion(),
                stat.getCantidad(),
                stat.getFechaStat()
        );
    }

    public void calcularEstadisticas() {
        List<StatDTO> stats = new ArrayList<>();
        AgregadorConnector agregadorConnector = new AgregadorConnector();

        stats.addAll(agregadorConnector.getCategoriaPorHechos());
        stats.addAll(agregadorConnector.getHechosDeColeccion());
        stats.addAll(agregadorConnector.getProviniciaCategoriaReportada());
        stats.addAll(agregadorConnector.getHoraMasReportada());

        StatDTO spam = agregadorConnector.getSpam();
        if (spam != null) {
            stats.add(spam);
        }

        List<Estadistica> estadisticas = stats.stream()
                .map(this::fromDTO)
                .collect(Collectors.toList());

        this.statRepository.saveAll(estadisticas);
    }

    public List<StatDTO> generateCSV() {
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<StatDTO> calcularProvinciaPorHechos(Long idColeccion){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StatDTO calcularCategoriaPorHechos() {
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .filter(e -> "CATEGORIA".equals(e.getTituloColeccion()))
                .max(Comparator.comparingLong(Estadistica::getCantidad))
                .map(this::toDTO)
                .orElse(null); // o puedes lanzar una excepción si prefieres
    }

    public List<StatDTO> calcularMaxHechos(Long idCategoria){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularHoraPico(Long idCategoria){
        List<Estadistica> estadisticas = statRepository.findAll();


        return estadisticas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<StatDTO> calcularSolicitudesPorSpam(){
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StatDTO calcularProvinciaMasReportadaPorColeccion(String nombreColeccion) {
        List<Estadistica> estadisticas = statRepository.findAll();

        return estadisticas.stream()
                .filter(est -> est.getTituloColeccion().equalsIgnoreCase(nombreColeccion))
                .collect(Collectors.groupingBy(
                        Estadistica::getDescripcion,
                        Collectors.summingLong(Estadistica::getCantidad)
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    StatDTO dto = new StatDTO();
                    dto.setTituloColeccion(nombreColeccion);
                    dto.setDescripcion(entry.getKey());
                    dto.setCantidad(entry.getValue());
                    return dto;
                })
                .orElse(null); // o puedes lanzar una excepción si prefieres
    }
}
