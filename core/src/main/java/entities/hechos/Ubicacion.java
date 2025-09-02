package entities.hechos;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    public Ubicacion(String latitud, String longitud, Localidad localidad) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.localidad = localidad;
    }
}
