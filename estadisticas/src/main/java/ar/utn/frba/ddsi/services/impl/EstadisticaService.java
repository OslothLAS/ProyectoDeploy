package ar.utn.frba.ddsi.services.impl;

import ar.utn.frba.ddsi.models.entities.Estadistica;
import ar.utn.frba.ddsi.models.repositories.IStatRepository;
import ar.utn.frba.ddsi.services.IEstadisticaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadisticaService implements IEstadisticaService {
    private IStatRepository statRepository;

    public EstadisticaService(IStatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public void calcularEstadisticas(){

    }

    public List<Estadistica> calcularProvinciaPorHechos(Long idColeccion){
        //con el webclient pegarle a la API de Agregador e instanciar y persistir las estadisticas, tambien
        //devolverlas

        return new ArrayList<>();
    }

    public List<Estadistica> calcularCategoriaPorHechos(){
        return new ArrayList<>();
    }

    public List<Estadistica> calcularMaxHechos(Long idCategoria){
        return new ArrayList<>();
    }

    public List<Estadistica> calcularHoraPico(Long idCategoria){
        return new ArrayList<>();
    }

    public List<Estadistica> calcularSolicitudesPorSpam(){
        return new ArrayList<>();
    }
}
