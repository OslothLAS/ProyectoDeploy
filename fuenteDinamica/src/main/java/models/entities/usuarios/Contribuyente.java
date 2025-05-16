package models.entities.usuarios;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente implements Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;
    private Boolean registrado;
    @Override
    public String getNombre() {
        return nombre;
    }

    public Integer obtenerEdad() {
        if (fechaDeNacimiento == null) {
            return null;
        }
        return Period.between(fechaDeNacimiento, LocalDate.now()).getYears();
    }

    @Override
    public Boolean getRegistrado() {
        return registrado;
    }
}