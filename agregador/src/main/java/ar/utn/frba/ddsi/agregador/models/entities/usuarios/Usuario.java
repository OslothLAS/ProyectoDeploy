package ar.utn.frba.ddsi.agregador.models.entities.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;


    public Usuario(String nombre, String apellido, LocalDate fechaNacimiento, TipoUsuario tipo, String username) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }
}
