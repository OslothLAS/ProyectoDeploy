package entities.hechos;

import entities.eliminacion.SolicitudEliminacion;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private String autor;
    private Boolean esValido;
    private DatosHechos datosHechos;
    private Multimedia multimedia;

    public static Hecho create(DatosHechos datosHechos, String autor) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(autor)
                .build();
    }

    public static Hecho create(DatosHechos datosHechos, String autor, Multimedia multimedia) {
        return Hecho.builder()
                .datosHechos(datosHechos)
                .esValido(true)
                .autor(autor)
                .multimedia(multimedia)
                .build();
    }
}
