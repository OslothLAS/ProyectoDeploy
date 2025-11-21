package ar.utn.ba.ddsi.fuenteDinamica.models.entities.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Rol tipo;

    public Usuario(String nombre, String apellido, LocalDate fechaNacimiento, Rol tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }
}
