package ar.utn.ba.ddsi.fuenteProxy.dtos.solicitud;

public class SolicitudesInputDto {
    private Long id_solicitante;
    private Long id_hecho;
    private String justificacion;

    public Long getId_hecho() {
        return id_hecho;
    }

    public Long getId_solicitante() {
        return id_solicitante;
    }

    public String getJustificacion() {
        return justificacion;
    }
}

