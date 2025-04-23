package escenarios;

import entities.colecciones.Coleccion;
import entities.eliminacion.Contribuyente;
import entities.eliminacion.EstadoSolicitudEliminacion;
import entities.eliminacion.SolicitudEliminacion;
import entities.fuentes.FuenteEstatica;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Ubicacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static entities.eliminacion.EstadoSolicitudEliminacion.ACEPTADA;
import static entities.eliminacion.EstadoSolicitudEliminacion.RECHAZADA;
import static entities.hechos.Origen.CARGA_MANUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Escenario3Test {
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

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );

        assertEquals(EstadoSolicitudEliminacion.PENDIENTE, solicitud.getEstado());


        solicitud.cambiarEstadoHecho(RECHAZADA);
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

        SolicitudEliminacion solicitud = new SolicitudEliminacion(
                justificacion,
                hecho,
                contribuyente
        );


        solicitud.cambiarEstadoHecho(ACEPTADA);
        solicitud.setFechaDeEvaluacion(solicitud.getFechaDeCreacion().plusHours(2));
        assertEquals(2, ChronoUnit.HOURS.between(solicitud.getFechaDeCreacion(),solicitud.getFechaDeEvaluacion()));

        //no se puede agregar un hecho, no es valido
        FuenteEstatica fuente = new FuenteEstatica();
        Coleccion coleccion = new Coleccion("Colección prueba 3", "Esto es una prueba", fuente);
        assertThrows(RuntimeException.class, () -> coleccion.addHecho(hecho), "RuntimeException: El hecho no es válido");

        //la solicitud queda aceptada
        assertEquals(ACEPTADA, solicitud.getEstado());
        assertEquals(false, hecho.getEsValido());
    }
}
