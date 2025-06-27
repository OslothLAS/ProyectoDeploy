package ar.utn.ba.ddsi.fuenteProxy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.colecciones.Handle;

import java.util.List;

public class HechoDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private Boolean esValido;
    private List<Long> colecciones;

    @JsonProperty("fecha_hecho")
    private String fechaHecho;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public List<Long> getColecciones() {
        return colecciones;
    }

    public String getFechaHecho() {
        return fechaHecho;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getEsValido() {
        return esValido;
    }
}
