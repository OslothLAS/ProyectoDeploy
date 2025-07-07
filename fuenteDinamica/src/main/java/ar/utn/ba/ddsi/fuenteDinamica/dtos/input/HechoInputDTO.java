package ar.utn.ba.ddsi.fuenteDinamica.dtos.input;

import entities.hechos.DatosHechos;
import entities.hechos.Multimedia;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HechoInputDTO {
    private DatosHechos datosHechos;
    private Multimedia multimedia;

    private Long idUsuario; //esto lo vemos despues (id del usuario)
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Boolean mostrarDatos;
}
