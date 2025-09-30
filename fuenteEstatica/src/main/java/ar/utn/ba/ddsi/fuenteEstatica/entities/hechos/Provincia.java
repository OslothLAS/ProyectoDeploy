package ar.utn.ba.ddsi.fuenteEstatica.entities.hechos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Provincia {
    private String nombre;

    public Provincia(String nombre) {
        this.nombre = nombre;
    }
}
