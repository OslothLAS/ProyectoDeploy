package models.entities.solicitudes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetectorDeSpamTest {

    @Test
    public void detectarSpam() {
        DetectorDeSpam detectoDeSpam = new DetectorDeSpam();
        String text = "Oferta, gratis un auto 0KM";
        assertTrue(detectoDeSpam.isSpam(text));
    }

    @Test
    public void detectarTextoValido() {
        DetectorDeSpam detectoDeSpam = new DetectorDeSpam();
        String text = "Buenas tardes, quiero reportar un hecho";
        assertFalse(detectoDeSpam.isSpam(text));
    }
}