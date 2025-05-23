package schedulers;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ColeccionScheduler {

    private ColeccionScheduler coleccionService;

    public ColeccionScheduler(ColeccionScheduler coleccionService) {
        this.coleccionService = coleccionService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void actualizarHechos(){
        this.coleccionService.actualizarHechos();
    }
}
