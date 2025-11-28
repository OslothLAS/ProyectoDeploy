package ar.utn.ba.ddsi.fuenteEstatica.entities.hechos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
public class Ubicacion {
    private String latitud;
    private String longitud;
    private Localidad localidad;

    public void setLatitud(String latitud) {
        if (latitud != null && !latitud.isBlank()) {
            double valor = Double.parseDouble(latitud);

            this.latitud = String.format(Locale.US, "%.4f", valor);
        } else {
            this.latitud = latitud;
        }
    }

    public void setLongitud(String longitud) {
        if (longitud != null && !longitud.isBlank()) {
            double valor = Double.parseDouble(longitud);

            this.longitud = String.format(Locale.US, "%.4f", valor);
        } else {
            this.longitud = longitud;
        }
    }

    public Ubicacion(String latitud, String longitud, Localidad localidad) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
    }
}
