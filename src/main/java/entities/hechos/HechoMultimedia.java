package entities.hechos;

import java.time.LocalDateTime;

public class HechoMultimedia extends Hecho{
    private String multimedia;

    public HechoMultimedia (String titulo, String descripcion, String categoria, Ubicacion ubicacion, LocalDateTime fechaHecho, Origen origen, String multimedia) {
        super(titulo, descripcion, categoria, ubicacion, fechaHecho, origen);
        this.multimedia = multimedia;
    }
}
