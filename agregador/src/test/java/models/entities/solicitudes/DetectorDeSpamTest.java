package models.entities.solicitudes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetectorDeSpamTest {

    @Test
    public void detectarSpam() {
        DetectorDeSpam detectoDeSpam = new DetectorDeSpam();
        String text = "Oferta, gratis un auto 0KM";
        assertEquals(detectoDeSpam.isSpam(text), true);
    }
}