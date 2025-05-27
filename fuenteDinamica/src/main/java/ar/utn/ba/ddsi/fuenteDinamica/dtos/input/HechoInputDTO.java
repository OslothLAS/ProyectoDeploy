package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import entities.hechos.Multimedia;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDate fechaHecho;

    private Multimedia multimedia;

    private Long idUsuario; //esto lo vemos despues (id del usuario)
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Boolean mostrarDatos;
}
