package ar.utn.ba.ddsi.fuenteDinamica.models.entities.multimedia;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface IAlmacenamientoStrategy {

    String guardarArchivo(MultipartFile archivo) throws IOException;
}