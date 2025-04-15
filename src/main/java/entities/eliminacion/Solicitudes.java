package entities.eliminacion;

import java.util.List;

public class Solicitudes {
    private List<SolicitudEliminacion> solicitudes;

    public void cargarSolicitud(SolicitudEliminacion solicitudEliminacion){
        this.solicitudes.add(solicitudEliminacion);
    }
}
