package entities.hechos;

import entities.eliminacion.SolicitudEliminacion;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
public class Hecho {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaHecho;
    private LocalDate fechaCarga;
    private Origen origen;
    private Boolean estado;

    public Hecho(String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDate fechaHecho, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaHecho = fechaHecho;
        this.fechaCarga = LocalDate.now();
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
