package services.impl;

import config.HechoProperties;
import models.entities.hechos.Hecho;
import models.entities.usuarios.Usuario;
import models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.IHechoService;

import java.time.Duration;

@Service
public class HechoService implements IHechoService {
    @Autowired  // ← Opcional en Spring Boot
    private HechoProperties hechoProperties;
    private final IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }
    @Override
    public void crearHecho(Hecho hecho) {
        if(hecho.getUsuario().getRegistrado()) {
            hecho.setPlazoEdicion(Duration.ofDays((hechoProperties.getPlazoEdicionDias())));
            this.hechoRepository.save(hecho);
        }else{
            hecho.setEsEditable(false);
            this.hechoRepository.save(hecho);
        }
    }

    @Override
    public void editarHecho(Hecho hecho) {
        if(!hecho.getUsuario().getRegistrado()) {
            throw new RuntimeException(); //hacer excepcion pq no se puede editar
        }else if (hecho.esEditable()) {
            this.hechoRepository.edit(hecho);
        }

    }
}
