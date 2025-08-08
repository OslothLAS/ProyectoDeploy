package models.entities.solicitudes;

import entities.solicitudes.SolicitudEliminacion;
import entities.usuarios.Contribuyente;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static entities.solicitudes.PosibleEstadoSolicitud.PENDIENTE;
import static entities.solicitudes.PosibleEstadoSolicitud.RECHAZADA;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SolicitudEliminacionTest {

    @Test
    public void rechazarPorSpam() {
        String just = "Oferta, gratis un auto 0KM";
        Long idHecho = 10L;

        Contribuyente contribuyente = new Contribuyente(10L, "Osmar", "Silvero", LocalDate.now());
        SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(just, idHecho, contribuyente);
        assertEquals(RECHAZADA, solicitudEliminacion.getEstado());

    }

    @Test
    public void dejarLaSolicitudEnPendiente() {
        String just = "Buenos dias, solicito la eliminacion de el hecho acerca de incendios porque es falsa";
        Long idHecho = 10L;

        Contribuyente contribuyente = new Contribuyente(10L, "Osmar", "Silvero", LocalDate.now());
        SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(just, idHecho, contribuyente);
        assertEquals(PENDIENTE, solicitudEliminacion.getEstado());

    }
}