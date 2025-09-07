package ar.utn.frba.ddsi.agregador;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static utils.NormalizadorTexto.normalizarTexto;
import static utils.NormalizadorTexto.normalizarTrimTexto;

@SpringBootTest
class AgregadorApplicationTests {

	@Test
	void contextLoads() {
        var str = "HoLÁ QUE Onda ";

        System.out.println(normalizarTrimTexto(str));

        Assertions.assertNotSame(str, normalizarTrimTexto(str));
	}

}
