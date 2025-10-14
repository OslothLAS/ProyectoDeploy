package com.frontMetaMapa.frontMetaMapa.models.dtos.output;

import lombok.*;

import java.time.LocalDate;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipo;
}
