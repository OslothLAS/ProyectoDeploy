package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Multimedia;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.multimedia.IAlmacenamientoStrategy;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Service
public class MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private IHechoRepository hechoRepository;

    @Autowired
    private ApplicationContext context;


    @Value("${app.storage.type}")
    private String storageType;

    private IAlmacenamientoStrategy storageStrategy;

    @PostConstruct
    public void init() {
        String beanName = storageType.equals("cloud") ? "cloudStorage" : "localStorage";
        this.storageStrategy = (IAlmacenamientoStrategy) context.getBean(beanName);

        System.out.println(">>> [MultimediaService] Estrategia activa: " + beanName);
    }

    public void guardar(Long idHecho, MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) return;

        Hecho hecho = hechoRepository.findById(idHecho)
                .orElseThrow(() -> new RuntimeException("Hecho no encontrado"));


        String urlRecurso = storageStrategy.guardarArchivo(archivo);


        Multimedia media = new Multimedia();
        media.setUrl(urlRecurso);
        media.setHecho(hecho);

        multimediaRepository.saveAndFlush(media);
    }
}