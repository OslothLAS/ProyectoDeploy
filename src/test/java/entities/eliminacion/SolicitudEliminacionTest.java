package entities.eliminacion;

import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Ubicacion;
import entities.usuarios.Administrador;
import entities.usuarios.Contribuyente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static entities.eliminacion.EstadoSolicitudEliminacion.*;
import static entities.hechos.Origen.CARGA_MANUAL;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudEliminacionTest {

    @Test
    @DisplayName("Se crea una nueva solicitud de eliminacion, el administrador la rechaza y el hecho sigue visible")
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
        Administrador admin = new Administrador();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );

        assertEquals(EstadoSolicitudEliminacion.PENDIENTE, solicitud.getEstado());


        solicitud.cambiarEstadoHecho(admin, RECHAZADA);
        solicitud.setFechaDeEvaluacion(solicitud.getFechaDeCreacion().plusDays(1));

        assertEquals(1, ChronoUnit.DAYS.between(solicitud.getFechaDeCreacion(),solicitud.getFechaDeEvaluacion()));
        assertEquals(RECHAZADA, solicitud.getEstado());
        assertEquals(true, hecho.getEsValido());

    }

    @Test
    @DisplayName("Se acepta la solicitud de eliminacion y el hecho cambia de estado a no visible")
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
        Administrador admin = new Administrador();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );


        solicitud.cambiarEstadoHecho(admin, ACEPTADA);
        solicitud.setFechaDeEvaluacion(solicitud.getFechaDeCreacion().plusHours(2));
        assertEquals(2, ChronoUnit.HOURS.between(solicitud.getFechaDeCreacion(),solicitud.getFechaDeEvaluacion()));


        //la solicitud queda aceptada
        assertEquals(ACEPTADA, solicitud.getEstado());
        assertEquals(false, hecho.getEsValido());
    }

    @Test
    @DisplayName("Se guarda una solicitud en EstadoSolicitud")
    public void testGuardarSolicitud() {
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
        Administrador admin = new Administrador();
        Administrador admin1 = new Administrador();

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );

        //RECHAZO LA SOLICITUD Y LO GUARDO EN EL INDEX 0 DE LA LISTA DE ESTADOS

        solicitud.cambiarEstadoHecho(admin, RECHAZADA);
        EstadoSolicitud estadoSolicitud = solicitud.getHistorialDeSolicitud().get(0);




        //Compruebo que la solicitud tenga guardado al estado, que el administrador sea admin y que este rechazada

        assertEquals(true, solicitud.getHistorialDeSolicitud().contains(estadoSolicitud));
        assertEquals(admin, estadoSolicitud.getAdministrador());
        assertEquals(RECHAZADA, estadoSolicitud.getEstado());




        //ACEPTO LA SOLICITUD Y LO GUARDO EN EL INDEX 1 DE LA LISTA DE ESTADOS DE LA SOLICITUD

        solicitud.cambiarEstadoHecho(admin1, ACEPTADA);
        EstadoSolicitud estadoSolicitud1 = solicitud.getHistorialDeSolicitud().get(1);



        //Compruebo que cambie el estado a true de la segunda solicitud, que el aministrador

        //sea admin1 y que quede ACEPTADA
        assertEquals(true, solicitud.getHistorialDeSolicitud().contains(estadoSolicitud1));
        assertEquals(admin1, estadoSolicitud1.getAdministrador());
        assertEquals(ACEPTADA, estadoSolicitud1.getEstado());




        //LA SOLICITUD TIENE 2 ELEMENTOS estadoSolicitud y estadoSolicitud1

        assertEquals(2, solicitud.getHistorialDeSolicitud().size());

    }
}
