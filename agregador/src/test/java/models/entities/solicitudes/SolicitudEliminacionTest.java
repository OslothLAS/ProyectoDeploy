package models.entities.solicitudes;

import ar.utn.frba.ddsi.agregador.models.entities.solicitudes.SolicitudEliminacion;
import ar.utn.frba.ddsi.agregador.models.entities.usuarios.Contribuyente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion.RECHAZADA;
import static entities.solicitudes.EstadoSolicitudEliminacion.ACEPTADA;
import static ar.utn.frba.ddsi.agregador.models.entities.solicitudes.EstadoSolicitudEliminacion.PENDIENTE;
import static org.junit.jupiter.api.Assertions.*;

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