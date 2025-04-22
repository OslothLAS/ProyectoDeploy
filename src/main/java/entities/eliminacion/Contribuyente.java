package entities.eliminacion;

import entities.hechos.Hecho;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente {
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;

    public Integer obtenerEdad() {
        if (fechaDeNacimiento == null) {
            return null;
        }
        return Period.between(fechaDeNacimiento, LocalDate.now()).getYears();
    }

}