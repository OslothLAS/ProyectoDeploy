package entities.criteriosDePertenencia;

import entities.hechos.Hecho;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.time.LocalDate;

@Getter
public class CriterioPorFecha implements CriterioDePertenencia{
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String tipo;

    public CriterioPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = "fecha";
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        LocalDate fechaHecho = hecho.getDatosHechos().getFechaHecho();
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
