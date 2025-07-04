package ar.utn.ba.ddsi.fuenteProxy.dtos.hecho;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HechoDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private Boolean esValido;
    private Boolean esConsensuado;
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

    public Boolean getEsConsensuado() {
        return esConsensuado;
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
