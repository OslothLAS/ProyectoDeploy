package entities.usuarios;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {
    }

    public Visualizador(@JsonProperty("nombre") String nombre,
                        @JsonProperty("apellido") String apellido,
                        @JsonProperty("fechaDeNacimiento") LocalDate fechaDeNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}

