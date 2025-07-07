package ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud;

import java.util.List;

public class SolicitudDto {
    private Long id;
    private Long id_solicitante;
    private Long id_hecho;
    private String fechaDeCreacion;
    private String fechaDeEvaluacion;
    private String justificacion;
    private String estadoSolicitudEliminacion;
    private List<String> historialDeSolicitud;

    public SolicitudDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_solicitante() {
        return id_solicitante;
    }

    public void setId_solicitante(Long id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public Long getId_hecho() {
        return id_hecho;
    }

    public void setId_hecho(Long id_hecho) {
        this.id_hecho = id_hecho;
    }

    public String getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(String fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public String getFechaDeEvaluacion() {
        return fechaDeEvaluacion;
    }

    public void setFechaDeEvaluacion(String fechaDeEvaluacion) {
        this.fechaDeEvaluacion = fechaDeEvaluacion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getEstadoSolicitudEliminacion() {
        return estadoSolicitudEliminacion;
    }

    public void setEstadoSolicitudEliminacion(String estadoSolicitudEliminacion) {
        this.estadoSolicitudEliminacion = estadoSolicitudEliminacion;
    }

    public List<String> getHistorialDeSolicitud() {
        return historialDeSolicitud;
    }

    public void setHistorialDeSolicitud(List<String> historialDeSolicitud) {
        this.historialDeSolicitud = historialDeSolicitud;
    }

    public void agregarEstadoHistorial(String estadoSolicitudEliminacion) {
        this.historialDeSolicitud.add(estadoSolicitudEliminacion);
    }
}
