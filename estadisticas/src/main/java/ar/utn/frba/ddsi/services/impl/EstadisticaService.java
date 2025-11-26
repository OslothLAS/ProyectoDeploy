package ar.utn.frba.ddsi.services.impl;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.models.entities.AgregadorConnector;
import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.models.repositories.IStatRepository;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticaService implements IEstadisticaService {

    private final IStatRepository statRepository;
    private final AgregadorConnector agregadorConnector;

    public EstadisticaService(
            IStatRepository statRepository,
            AgregadorConnector agregadorConnector
    ) {
        this.statRepository = statRepository;
        this.agregadorConnector = agregadorConnector;
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

    public List<StatDTO> findAll(){
        return this.statRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void calcularEstadisticas() {
        List<StatDTO> stats = new ArrayList<>();

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


    // Pregunta 1: De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos?
    public StatDTO getProvinciaMasReportadaPorColeccion(String nombreColeccion) {
        Estadistica est = statRepository.findTopProvinciaByColeccion(nombreColeccion);
        return est != null ? this.toDTO(est) : null;
    }

    // Pregunta 2: ¿Cuál es la categoría con mayor cantidad de hechos reportados?
    public StatDTO getCategoriaConMasHechos() {
        Estadistica est = statRepository.findTopCategoria();
        return est != null ? this.toDTO(est) : null;
    }

    // Pregunta 3: ¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
    public StatDTO getProvinciaConMasHechosDeCategoria(String categoria) {
        Estadistica est = statRepository.findTopProvinciaByCategoria(categoria);
        return est != null ? this.toDTO(est) : null;
    }

    // Pregunta 4: ¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
    public StatDTO getHoraPicoDeCategoria(String categoria) {
        Estadistica est = statRepository.findTopHoraByCategoria(categoria);
        return est != null ? this.toDTO(est) : null;
    }

    // Pregunta 5: ¿Cuántas solicitudes de eliminación son spam?
    public StatDTO getCantidadDeSpam() {
        Estadistica est = statRepository.findLastSpamCount();
        return est != null ? this.toDTO(est) : null;
    }
}
