package ar.utn.frba.ddsi.agregador.models.entities.criteriosDePertenencia;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "criterio_fecha")
@DiscriminatorValue("fecha")
public class CriterioPorFecha extends CriterioDePertenencia {
    @Column(name = "fecha_desde")
    private final LocalDateTime fechaInicio;
    @Column(name = "fecha_hasta")
    private final LocalDateTime fechaFin;

    @Transient
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
