package entities.usuarios;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalDate;
import java.time.Period;


@Getter
public class Contribuyente implements Usuario {
    private long id;
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaDeNacimiento;

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
        return true;
    }

    public Contribuyente(@JsonProperty("id") Long id, @JsonProperty("nombre") String nombre,
                         @JsonProperty("apellido") String apellido,
                         @JsonProperty("fechaDeNacimiento") LocalDate fechaDeNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.apellido = apellido;
    }

    public Boolean esAdministrador() {return false;}

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}