package ar.utn.ba.ddsi.fuenteDinamica.models.entities.multimedia;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service("cloudStorage")
public class CloudStorageStrategy implements IAlmacenamientoStrategy {

    @Override
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        // Lógica futura para conectar con AWS S3, Google Cloud, Azure, etc.
        System.out.println(">>> [CloudStorage] Subiendo a la nube...");

        // Simulación
        String urlNube = "https://mi-bucket-s3.aws.com/" + archivo.getOriginalFilename();

        return urlNube;
    }
}