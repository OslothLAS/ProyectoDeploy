package ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion;

import ar.utn.ba.ddsi.fuenteProxy.dtos.coleccion.CriterioPertenenciaDto;

import java.util.List;


public class ColeccionInputDto {
    private String titulo;

    private String descripcion;

    private List<CriterioPertenenciaDto> criteriosDePertenencia;

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

    public List<CriterioPertenenciaDto> getCriteriosDePertenencia() {
        return criteriosDePertenencia;
    }

    public void setCriteriosDePertenencia(List<CriterioPertenenciaDto> criteriosDePertenencia) {
        this.criteriosDePertenencia = criteriosDePertenencia;
    }
}