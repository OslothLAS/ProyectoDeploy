package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Builder
@Getter
@Setter
public class DatosHechos {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;

    public DatosHechos(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDate fechaHecho) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaHecho = fechaHecho;
    }
}
