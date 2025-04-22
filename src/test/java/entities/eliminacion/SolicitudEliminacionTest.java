package entities.eliminacion;

import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static entities.eliminacion.EstadoSolicitudEliminacion.ACEPTADA;
import static entities.hechos.Origen.CARGA_MANUAL;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudEliminacionTest {

    @Test
    public void testCrearSolicitudPendiente() {
        Ubicacion ubicacion = new Ubicacion("-32.786098","-60.741543");
        DatosHechos datos =  new DatosHechos(
                "Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe",
                "Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
                "Evento sanitario",
                          ubicacion,
                LocalDate.of(2005, 7, 5),
                LocalDate.now(),
                CARGA_MANUAL
        );

        String justificacion = "a".repeat(500);

        Hecho hecho = new Hecho();
        hecho.setAutor("Osmar");
        hecho.setDatosHechos(datos);

        Contribuyente contribuyente = new Contribuyente();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );
        assertEquals(solicitud.getEstado(), EstadoSolicitudEliminacion.PENDIENTE);

        solicitud.estadoDeSolicitud(ACEPTADA);
        assertEquals(solicitud.getEstado(), ACEPTADA);
    }

}