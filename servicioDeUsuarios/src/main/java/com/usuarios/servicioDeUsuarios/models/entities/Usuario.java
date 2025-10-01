package com.usuarios.servicioDeUsuarios.models.entities;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "username")
    private String username;

    @Column(name= "contrasenia")
    private String contrasenia;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    //private List<Permiso> permisos;

    public Usuario(String username, String contrasenia, String nombre, String apellido, LocalDate fechaNacimiento, Rol rol) {
        this.username = username;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
    }
}
