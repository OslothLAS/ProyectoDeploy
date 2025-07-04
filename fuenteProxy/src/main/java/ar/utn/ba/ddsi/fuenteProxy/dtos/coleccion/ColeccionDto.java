package ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public List<CriterioPertenenciaDto> getCriteriosDePertenencia() {
        return criteriosDePertenencia;
    }

    public void setCriteriosDePertenencia(List<CriterioPertenenciaDto> criteriosDePertenencia) {
        this.criteriosDePertenencia = criteriosDePertenencia;
    }

    public String getFechaYHoraDeActualizacion() {
        return fechaYHoraDeActualizacion;
    }

    public void setFechaYHoraDeActualizacion(String fechaYHoraDeActualizacion) {
        this.fechaYHoraDeActualizacion = fechaYHoraDeActualizacion;
    }
}
