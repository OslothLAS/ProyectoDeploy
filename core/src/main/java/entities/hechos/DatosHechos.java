package entities.hechos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class DatosHechos {
    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(name = "fecha")
    private LocalDate fechaHecho;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatosHechos that = (DatosHechos) o;

        return Objects.equals(titulo, that.titulo) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(categoria, that.categoria) &&
                Objects.equals(ubicacion, that.ubicacion) &&
                Objects.equals(fechaHecho, that.fechaHecho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, descripcion, categoria, ubicacion, fechaHecho);
    }

    @Override
    public String toString() {
        return "DatosHechos{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", ubicacion=" + ubicacion +
                ", fechaHecho=" + fechaHecho +
                '}';
    }

    @Builder
    public DatosHechos(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fechaHecho) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaHecho = fechaHecho;
        this.categoria = categoria;
    }

    public void normalizarDatos(){


    }


}
