package entities.hechos;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DatosHechos {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
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
}
