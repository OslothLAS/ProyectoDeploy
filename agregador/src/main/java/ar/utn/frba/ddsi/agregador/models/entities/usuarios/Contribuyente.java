package ar.utn.frba.ddsi.agregador.models.entities.usuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;


@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name = "contribuyente")
public class Contribuyente  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String apellido;
    //private LocalDate fechaDeNacimiento;
/*
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

    @Override
    public void setId(Long id) {

    }

    @Override
    public Long getId() { return this.id; }

    public Contribuyente(Long id, String nombre, String apellido, LocalDate fechaDeNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.apellido = apellido;
    }

    @JsonCreator
    public Contribuyente(
            @JsonProperty("id") long id,
            @JsonProperty("nombre") String nombre,
            @JsonProperty("apellido") String apellido,
            @JsonProperty("fechaDeNacimiento") LocalDate fechaDeNacimiento
    ) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Boolean esAdministrador() {return false;}

 */
}