package entities.eliminacion;

import entities.hechos.Hecho;

public class SolicitudEliminacion {
    private Contribuyente solicitante;

    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private Hecho hecho;

    public void cambiarEstadoHecho(){
        this.estado = EstadoSolicitudEliminacion.ACEPTADA;
    }

    public void justificarSolicitud(String justificacionSolicitud) {
        if (justificacionSolicitud == null || justificacionSolicitud.length() < 500) {
            throw new IllegalArgumentException("La justificacion debe tener al menos 500 caracteres");
        }
        else{
            this.justificacion = justificacionSolicitud;
        }

    }
}
