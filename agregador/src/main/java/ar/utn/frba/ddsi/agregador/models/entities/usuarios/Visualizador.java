package ar.utn.frba.ddsi.agregador.models.entities.usuarios;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Visualizador implements Usuario {
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaDeNacimiento;

    @Override
    public Boolean getRegistrado() {
        return false;
    }
//    @Override
//    public Boolean esAdministrador() {
//        return false;
//    }

    public Visualizador(String nombre, String apellido, LocalDate fechaDeNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}
