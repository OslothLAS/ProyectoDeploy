package entities.hechos;

import entities.eliminacion.SolicitudEliminacion;

import java.time.LocalDateTime;

public abstract class Hecho {
    protected String titulo;
    protected String descripcion;
    protected String categoria;
    protected Ubicacion ubicacion;
    protected LocalDateTime fechaHecho;
    protected LocalDateTime fechaCarga;
    protected Origen origen;
    protected Boolean estado;

    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDateTime fechaHecho, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaHecho = fechaHecho;
        this.fechaCarga = LocalDateTime.now();
        this.origen = origen;
        this.estado = true; //
    }

    public void solicitarElimnacion(String justificacion){
        if(justificacion.length() > 500){
            SolicitudEliminacion solicitud = new SolicitudEliminacion(justificacion, this);
            // Aqui se deberia agregar la solicitud a una lista de solicitudes
            // Solicitudes.cargarSolicitud(solicitud);
        }
    }
}
