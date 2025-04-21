package entities.eliminacion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudEliminacionTest {

    @Test
    public void testJustificarSolicitud_Exitoso() {

        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        String justificacion = "a".repeat(500);

        assertDoesNotThrow(() -> solicitud.justificarSolicitud(justificacion));
    }

    @Test
    public void testJustificarSolicitud_DemasiadoCorto() {
        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        String justificacionCorta = "a".repeat(499);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> solicitud.justificarSolicitud(justificacionCorta));

        assertEquals("La justificacion debe tener al menos 500 caracteres", exception.getMessage());
    }

    @Test
    public void testJustificarSolicitud_Null() {
        SolicitudEliminacion solicitud = new SolicitudEliminacion();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> solicitud.justificarSolicitud(null));

        assertEquals("La justificacion debe tener al menos 500 caracteres", exception.getMessage());
    }

    @Test
    public void testJustificarSolicitud_MasDe500Caracteres() {
        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        String justificacionLarga = "a".repeat(501);

        assertDoesNotThrow(() -> solicitud.justificarSolicitud(justificacionLarga));
    }
}