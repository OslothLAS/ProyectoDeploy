package models.entities.solicitudes;

import entities.hechos.Hecho;
import entities.usuarios.Administrador;
import entities.usuarios.Contribuyente;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor

@Getter
public class SolicitudEliminacion {
    @Setter
    private Long id;
    //se crea al contribuyente
    private Contribuyente solicitante;
    private LocalDateTime fechaDeCreacion;
    @Setter
    private LocalDateTime fechaDeEvaluacion;
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private List<EstadoSolicitud> historialDeSolicitud;
    private Long idHecho;
    @Setter
    private Hecho hecho;


    public SolicitudEliminacion(String justificacion, Long idHecho, Contribuyente solicitante) {
        this.justificacion = this.validarJustificacion(justificacion);
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        this.estado = EstadoSolicitudEliminacion.PENDIENTE;
        this.idHecho = idHecho;
        this.historialDeSolicitud = new ArrayList<>();
    }

    //Las validaciones en el service
    public String validarJustificacion(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            return justificacionSolicitud;
        }

    }

    //TODAS ESTAS VANA  SER DEL AGREGADOR

    public void cambiarEstadoHecho(Administrador admin, EstadoSolicitudEliminacion estado) {
        if(estado == EstadoSolicitudEliminacion.RECHAZADA) {
            cambiarEstadoSolicitud(estado);
        }
        else if(estado == EstadoSolicitudEliminacion.ACEPTADA){
            cambiarEstadoSolicitud(estado);
            //si la solicitud es aceptada, se cambia el estado del hecho
            hecho.setEsValido(false);
        }
        this.actualizarHistorialDeOperacion(estado, admin);

    }

    private void cambiarEstadoSolicitud(EstadoSolicitudEliminacion estado) {
        this.estado = EstadoSolicitudEliminacion.valueOf(estado.name());
        this.fechaDeEvaluacion = LocalDateTime.now();
    }

    private void actualizarHistorialDeOperacion(EstadoSolicitudEliminacion estado, Administrador admin){
        EstadoSolicitud estadoSolicitud = new EstadoSolicitud();
        estadoSolicitud.guardarEstado(estado, admin, this);
        this.historialDeSolicitud.add(estadoSolicitud);
    }
}