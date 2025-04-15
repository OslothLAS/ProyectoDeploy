package entities.eliminacion;

import entities.hechos.Hecho;

public class SolicitudEliminacion {
    //private Contribuyente solicitante;
    // Falta hacer todo respecto al contribuyente
    private String justificacion;
    private EstadoSolicitudEliminacion estado;
    private Hecho hecho;

    public SolicitudEliminacion(String justificacion, Hecho hecho) {
        this.justificacion = justificacion;
        this.estado = EstadoSolicitudEliminacion.PENDIENTE;
        this.hecho = hecho;
    }
}
