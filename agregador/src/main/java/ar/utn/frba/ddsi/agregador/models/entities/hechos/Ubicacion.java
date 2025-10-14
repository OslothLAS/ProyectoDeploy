package ar.utn.frba.ddsi.agregador.models.entities.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ubicacion")
public class Ubicacion {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    public void setLatitud(String latitud) {
        if (latitud != null && !latitud.isBlank()) {
            double valor = Double.parseDouble(latitud);
            this.latitud = String.format("%.4f", valor); // 4 decimales, sin ceros de más
        } else {
            this.latitud = latitud;
        }
    }

    public void setLongitud(String longitud) {
        if (longitud != null && !longitud.isBlank()) {
            double valor = Double.parseDouble(longitud);
            this.longitud = String.format("%.4f", valor);
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
