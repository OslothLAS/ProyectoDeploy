package services.impl;

import models.entities.hechos.Hecho;
import models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.IHechoService;
@Service
public class HechoService implements IHechoService {

    private final IHechoRepository hechoRepository;

    public HechoService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }
    @Override
    public void crearHecho(Hecho hecho) {
        this.hechoRepository.save(hecho);
    }

    @Override
    public void editarHecho(Hecho hecho) {
        this.hechoRepository.edit(hecho);
    }
}
