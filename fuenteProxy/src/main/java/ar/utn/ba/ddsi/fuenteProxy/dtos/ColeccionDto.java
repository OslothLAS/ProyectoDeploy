package ar.utn.ba.ddsi.fuenteProxy.dtos;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import entities.criteriosDePertenencia.CriterioDePertenencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Getter
@Setter
public class ColeccionDto {
        private Long id;
        private String titulo;
        private String descripcion;
        private String fuente;
            private List<CriterioPertenenciaDto> criteriosDePertenencia;
        private String fechaYHoraDeActualizacion;
    }
