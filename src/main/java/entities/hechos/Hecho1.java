package entities.hechos;


import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Hecho1 {
    protected String titulo;
    protected String descripcion;
    protected String categoria;
    protected Ubicacion ubicacion;
    protected LocalDate fechaHecho;
    protected LocalDateTime fechaCarga;
    protected Boolean estado;

    public Hecho1(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDate fechaHecho) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaHecho = fechaHecho;
        this.fechaCarga = LocalDateTime.now();
        this.estado = true; //
    }
}