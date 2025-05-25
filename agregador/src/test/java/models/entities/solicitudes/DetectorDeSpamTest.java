package models.entities.solicitudes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DetectoDeSpamTest {

    @Test
    public void detectarSpam() {
        DetectoDeSpam detectoDeSpam = new DetectoDeSpam();
        String text = "Oferta, gratis un auto 0KM";
        assertEquals(detectoDeSpam.isSpam(text), true);
    }
}