package ar.utn.ba.ddsi.fuenteDinamica.utils;

import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.HechoDTO;
import ar.utn.ba.ddsi.fuenteDinamica.dtos.input.UbicacionDTO;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Categoria;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HechoUtilTest {

    @Test
    void hechoDTOtoHechoTest() {
        HechoDTO dto = new  HechoDTO("untitulo", "unadescripcion", "una cat",new UbicacionDTO("111","1111",null), LocalDateTime.now(),null,null,true);
        Hecho hecho = HechoUtil.hechoDTOtoHecho(dto);
        hecho.setEsEditable(true);
        System.out.println(hecho.getPlazoEdicion());
        System.out.println(hecho.getFechaCreacion());

        assertEquals("untitulo", hecho.getTitulo());
    }
}