package models.entities.solicitudes;

import models.entities.hechos.Hecho;
import entities.usuarios.Administrador;
import entities.usuarios.Contribuyente;
import lombok.*;
import models.repositories.IHechoRepository;
import services.IDetectorDeSpam;

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
    public IDetectorDeSpam detectorDeSpam;
    public IHechoRepository hechoRepository;


    public SolicitudEliminacion(String justificacion, Long idHecho, Contribuyente solicitante) {
        this.justificacion = justificacion;
        this.solicitante = solicitante;
        this.fechaDeCreacion = LocalDateTime.now();
        if(detectorDeSpam.isSpam(justificacion)){
            this.estado = EstadoSolicitudEliminacion.RECHAZADA;}
        else{
            this.estado = EstadoSolicitudEliminacion.PENDIENTE;};
        this.idHecho = idHecho;
        this.historialDeSolicitud = new ArrayList<>();
    }

    //TODAS ESTAS VANA  SER DEL AGREGADOR

    public void cambiarEstadoHecho(Administrador admin, EstadoSolicitudEliminacion estado) {
        if(estado == EstadoSolicitudEliminacion.RECHAZADA) {
            cambiarEstadoSolicitud(estado);
        }
        else if(estado == EstadoSolicitudEliminacion.ACEPTADA){
            cambiarEstadoSolicitud(estado);
            //si la solicitud es aceptada, se cambia el estado del hecho (26/5 ahora con idHecho)
            hechoRepository.findById(idHecho).setEsValido(false);

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