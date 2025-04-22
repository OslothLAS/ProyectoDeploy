package entities.hechos;

import entities.fuentes.FuenteEstatica;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class HechoTest {

    @Test
    void testToString() {
        FuenteEstatica fuente = new FuenteEstatica();
        var hechos = fuente.obtenerHechos();
        Hecho hecho1 = hechos.get("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones");

        System.out.println(hecho1.toString());

        assertNotNull(hecho1);
    }

    @Test
    void testEtiquetarHechoAeronave() {
        // 1. Setup - Create the hecho with mock data
        Ubicacion ubicacion = new Ubicacion("-36.868375", "-60.343297");
        LocalDate fechaHecho = LocalDate.of(2001, 11, 29);

        DatosHechos datos = new DatosHechos("Caída de aeronave impacta en Olavarría",
                "Grave caída de aeronave ocurrió en las inmediaciones de la ciudad de Olavarría, Buenos Aires.",
                "Caída de aeronave", ubicacion, fechaHecho, LocalDate.now(), Origen.CONTRIBUYENTE);// Required for addEtiqueta() to work

        Hecho hecho = Hecho.create(datos);

        hecho.addEtiqueta("Olavarría");
        hecho.addEtiqueta("Grave");

        // 3. Verify - Check both tags are retained
        assertAll("Verify tags are added and retained",
                () -> assertEquals(2, hecho.getEtiquetas().size(), "Debería tener 2 etiquetas"),
                () -> assertTrue(hecho.getEtiquetas().contains("Olavarría"), "Falta etiqueta 'Olavarría'"),
                () -> assertTrue(hecho.getEtiquetas().contains("Grave"), "Falta etiqueta 'Grave'")
        );
    }
}