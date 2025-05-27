package models.entities.solicitudes;

import models.entities.usuarios.Contribuyente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static models.entities.solicitudes.EstadoSolicitudEliminacion.RECHAZADA;
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

}