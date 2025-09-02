package entities.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "tipo")
    private TipoUsuario tipo;

    public Boolean getRegistrado(){
        return true;
    }

    public Usuario(String nombre, String apellido, LocalDate fechaNacimiento, TipoUsuario tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }
}
