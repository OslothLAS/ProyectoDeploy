package entities.usuarios;

import java.time.LocalDate;
import java.time.Period;

public class Contribuyente implements Usuario {
    private String nombre;
    private String apellido;
    private LocalDate fechaDeNacimiento;

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

}