package ar.utn.frba.ddsi.schedullers;

import ar.utn.frba.ddsi.services.impl.EstadisticaService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatScheduller {
    private final EstadisticaService estadisticaService;

    public StatScheduller(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void calcularStats(){
        this.estadisticaService.calcularEstadisticas();
    }
}
