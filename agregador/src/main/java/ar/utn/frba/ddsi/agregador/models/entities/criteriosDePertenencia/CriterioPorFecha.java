package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "criterio_fecha")
@DiscriminatorValue("fecha")
public class CriterioPorFecha extends CriterioDePertenencia {
    @Column(name = "fecha_desde")
    private final LocalDate fechaInicio;
    @Column(name = "fecha_hasta")
    private final LocalDate fechaFin;

    @Transient
    private final String tipo;

    public CriterioPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = "fecha";
    }

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        LocalDate fechaHecho = hecho.getFechaHecho().toLocalDate();
        if(fechaFin == null && fechaInicio != null){
            return !fechaHecho.isBefore(fechaInicio);
        }
        if(fechaFin != null && fechaInicio == null){
            return !fechaHecho.isAfter(fechaFin);
        }

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
