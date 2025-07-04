package ar.utn.frba.ddsi.agregador.navegacion;

import entities.colecciones.Coleccion;
import entities.hechos.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("IRRESTRICTO")
public class NavegacionIrrestrictaStrategy implements NavegacionStrategy {
    @Override
    public List<Hecho> navegar(Coleccion coleccion, List<Hecho> hechos) {
        return hechos; // Devuelve todos los hechos
    }
}
