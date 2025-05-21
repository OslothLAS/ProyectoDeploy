package ar.utn.ba.ddsi.fuenteDinamica.dtos;

import lombok.Getter;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Multimedia;

import java.time.LocalDate;

@Getter
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private LocalDate fechaHecho;
    private long id; //esto lo vemos despues (id del usuario)
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Boolean mostrarDatos;
    private Multimedia multimedia;
}
