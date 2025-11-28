package ar.utn.ba.ddsi.fuenteDinamica.models.entities.multimedia;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service("localStorage")
public class LocalStorageStrategy implements IAlmacenamientoStrategy {

    @Override
    public String guardarArchivo(MultipartFile archivo) throws IOException {

        String userHome = System.getProperty("user.home");
        Path directorioBase = Paths.get(userHome, "Pictures", "DDSI_Uploads");


        if (!Files.exists(directorioBase)) {
            Files.createDirectories(directorioBase);
        }


        String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaCompleta = directorioBase.resolve(nombreUnico);


        Files.write(rutaCompleta, archivo.getBytes());

        System.out.println(">>> [LocalStorage] Archivo guardado en: " + rutaCompleta);

        return "/uploads/" + nombreUnico;
    }
}