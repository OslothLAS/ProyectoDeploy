package ar.utn.frba.ddsi.agregador.dtos.input;


import ar.utn.frba.ddsi.agregador.models.entities.usuarios.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutorDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipo;
}
