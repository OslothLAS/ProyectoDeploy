package models.entities.solicitudes;

import entities.solicitudes.DetectorDeSpam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetectorDeSpamTest {
    @Test
    public void detectarSpam() {
        DetectorDeSpam detectorDeSpam = new DetectorDeSpam();
        String text = "Oferta, gratis un auto 0KM";
        assertTrue(detectorDeSpam.isSpam(text));
    }

    @Test
    public void detectarTextoValido() {
        DetectorDeSpam detectorDeSpam = new DetectorDeSpam();
        String text = "Buenas tardes, quiero reportar un hecho";
        assertFalse(detectorDeSpam.isSpam(text));
    }
}