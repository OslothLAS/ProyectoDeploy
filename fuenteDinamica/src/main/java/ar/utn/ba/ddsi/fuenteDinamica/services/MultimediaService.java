package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private IHechoRepository hechoRepository;

    // ⚠️ Asegúrate de que esta carpeta exista manualmente en tu Windows
    private final String RUTA_CARPETA = "C:/DDSIMAGENES/";

    public void guardar(Long idHecho, MultipartFile archivo) throws IOException {
        System.out.println(">>> [MultimediaService] Iniciando guardado para Hecho ID: " + idHecho);

        if (archivo.isEmpty()) {
            System.out.println(">>> [MultimediaService] Archivo vacío. Abortando.");
            return;
        }

        // 1. Buscar Hecho
        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));
        System.out.println(">>> [MultimediaService] Hecho encontrado: " + hecho.getTitulo());

        // 2. Guardar en Disco
        String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaCompleta = Paths.get(RUTA_CARPETA + nombreUnico);
        Files.write(rutaCompleta, archivo.getBytes());
        System.out.println(">>> [MultimediaService] Archivo escrito en disco: " + rutaCompleta.toString());

        // 3. Guardar en BD
        Multimedia media = new Multimedia();
        media.setUrl("/uploads/" + nombreUnico);
        media.setHecho(hecho);

        // IMPORTANTE: Usamos saveAndFlush para forzar el guardado inmediato y que salte error si falla
        Multimedia guardado = multimediaRepository.saveAndFlush(media);

        System.out.println(">>> [MultimediaService] Guardado en BD con ID: " + guardado.getId());
    }
}