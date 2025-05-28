package ar.utn.frba.ddsi.agregador.schedulers;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ar.utn.frba.ddsi.agregador.services.impl.ColeccionService;

@Component
public class ColeccionScheduler {

    private ColeccionService coleccionService;


    public ColeccionScheduler(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void actualizarColecciones(){
        this.coleccionService.actualizarHechos();
    }
}
