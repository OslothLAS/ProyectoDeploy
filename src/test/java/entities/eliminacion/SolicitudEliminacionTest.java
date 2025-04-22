package entities.eliminacion;

import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static entities.eliminacion.EstadoSolicitudEliminacion.ACEPTADA;
import static entities.eliminacion.EstadoSolicitudEliminacion.RECHAZADA;
import static entities.hechos.Origen.CARGA_MANUAL;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudEliminacionTest {

    @Test
    public void testCrearSolicitudRechazada() {
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
        //Creacion del hecho con el metodo create y los datos pedidos
        Hecho hecho = Hecho.create(datos);

        //Creacion de una solicitud
        String justificacion = "a".repeat(500);
        Contribuyente contribuyente = new Contribuyente();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );

        assertEquals(solicitud.getEstado(), EstadoSolicitudEliminacion.PENDIENTE);


        solicitud.cambiarEstadoHecho(RECHAZADA);
        solicitud.setFechaDeEvaluacion(solicitud.getFechaDeCreacion().plusDays(1));

        assertEquals(ChronoUnit.DAYS.between(solicitud.getFechaDeCreacion(),solicitud.getFechaDeEvaluacion()), 1);
        assertEquals(solicitud.getEstado(), RECHAZADA);
        assertEquals(hecho.getEsValido(), true);

    }

    @Test
    public void testCrearSolicitudAceptada() {
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
        //Creacion del hecho con el metodo create y los datos pedidos
        Hecho hecho = Hecho.create(datos);

        //Creacion de una solicitud
        String justificacion = "a".repeat(500);
        Contribuyente contribuyente = new Contribuyente();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );

        solicitud.cambiarEstadoHecho(ACEPTADA);
        solicitud.setFechaDeEvaluacion(solicitud.getFechaDeCreacion().plusHours(2));
        assertEquals(ChronoUnit.HOURS.between(solicitud.getFechaDeCreacion(),solicitud.getFechaDeEvaluacion()), 2);
        assertEquals(solicitud.getEstado(), ACEPTADA);
        assertEquals(hecho.getEsValido(), false);

    }

}