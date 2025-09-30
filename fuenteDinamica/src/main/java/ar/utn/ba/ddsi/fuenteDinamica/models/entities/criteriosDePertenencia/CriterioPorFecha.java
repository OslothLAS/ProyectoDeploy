package ar.utn.ba.ddsi.fuenteDinamica.models.entities.criteriosDePertenencia;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

@Getter
public class CriterioPorFecha extends CriterioDePertenencia {
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    
    private final String tipo;

    public CriterioPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = "fecha";
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        LocalDateTime fechaHecho = hecho.getFechaHecho();
        return !fechaHecho.isBefore(fechaInicio) && !fechaHecho.isAfter(fechaFin);
    }

    @Override
    public MultiValueMap<String, String> aQueryParam(){
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("fechaInicio", this.fechaInicio.toString());
        map.add("fechaFin", this.fechaFin.toString());
        return map;
    }
}
