package schedulers;


import models.entities.colecciones.Coleccion;
import models.repositories.IHechoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import services.impl.ColeccionService;

import java.util.List;

@Component
public class ColeccionScheduler {

    private ColeccionService coleccionService;
    private IHechoRepository hechoRepository;
    private List<Coleccion> colecciones;

    public ColeccionScheduler(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void actualizarColecciones(){
        this.coleccionService.actualizarHechos(this.hechoRepository, this.colecciones);
    }
}
