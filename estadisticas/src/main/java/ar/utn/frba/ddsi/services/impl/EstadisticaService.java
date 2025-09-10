package ar.utn.frba.ddsi.services.impl;

import ar.utn.frba.ddsi.dtos.StatDTO;
import ar.utn.frba.ddsi.models.entities.AgregadorConnector;
import ar.utn.frba.ddsi.models.repositories.IStatRepository;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        this.statRepository.saveAll(stats);
    }

    public List<StatDTO> generateCSV(){
        return statRepository.findAll();
    }


    public List<StatDTO> calcularProvinciaPorHechos(Long idColeccion){
        return this.statRepository.findAll();
    }

    public List<StatDTO> calcularCategoriaPorHechos(){
        return this.statRepository.findAll();
    }

    public List<StatDTO> calcularMaxHechos(Long idCategoria){
        return this.statRepository.findAll();
    }

    public List<StatDTO> calcularHoraPico(Long idCategoria){
        return this.statRepository.findAll();
    }

    public List<StatDTO> calcularSolicitudesPorSpam(){
        return this.statRepository.findAll();
    }
}
