package ar.utn.frba.ddsi.agregador.schedulers;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ar.utn.frba.ddsi.agregador.services.impl.ColeccionService;

@Component
public class ColeccionScheduler {
    private final ColeccionService coleccionService;

    public ColeccionScheduler(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void actualizarColecciones(){
        this.coleccionService.actualizarHechos();
    }

    /*  hay que tener en cuenta la cantidad de usuario que usen el sistema lo que se traduce en
    cantidad de hechos subidos por dia. Depende de esa variable hay que poner el tiempo del
    cronjob.            */
    @Scheduled(cron = "0 0 */6 * * *")
    public void consensuarHechos(){
        this.coleccionService.consensuarHechos();
    }
}
