package com.usuarios.servicioDeUsuarios.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.usuarios.servicioDeUsuarios.models.entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDTO {
    private String username;
    private String contrasenia;
    private String nombre;
    private String apellido;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private Rol rol;
}
