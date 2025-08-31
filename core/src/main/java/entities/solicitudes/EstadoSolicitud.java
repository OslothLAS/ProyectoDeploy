package entities.solicitudes;

import entities.usuarios.Administrador;
import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name ="estadoSolicitud")
public class EstadoSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEstadoSolicitud;

    @OneToOne
    private Administrador administrador;

    private PosibleEstadoSolicitud estado;
    private LocalDateTime fechaDeCambio;
    private LocalDateTime fechaDeCreacion;


    public EstadoSolicitud(Administrador administrador, PosibleEstadoSolicitud estado) {
        this.administrador = administrador;
        this.estado = estado;
        this.fechaDeCreacion = LocalDateTime.now();
    }


    private Long calcularTiempoDeRespuesta(LocalDateTime fecha1, LocalDateTime fecha2) {
        Duration duration = Duration.between(fecha1, fecha2);
        return duration.toHours();
    }

}

