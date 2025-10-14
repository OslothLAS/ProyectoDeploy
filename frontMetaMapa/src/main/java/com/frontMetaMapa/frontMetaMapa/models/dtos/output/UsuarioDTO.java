package com.frontMetaMapa.frontMetaMapa.models.dtos.output;

import java.time.LocalDate;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private TipoUsuario tipo;
}
