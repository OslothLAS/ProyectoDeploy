package ar.utn.ba.ddsi.fuenteEstatica.entities.hechos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Localidad {
    private Provincia provincia;
    private String nombre;

    public Localidad(Provincia provincia, String nombre) {
        this.provincia = provincia;
        this.nombre = nombre;
    }
}

