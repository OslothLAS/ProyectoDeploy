package entities.hechos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class HechoTexto extends Hecho{
    private String texto;

    public HechoTexto(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDateTime fechaHecho, Origen origen, String texto) {
        super(titulo, descripcion, categoria, ubicacion, fechaHecho, origen);
        this.texto = texto;
    }
}
