package entities.colecciones;

import entities.criteriosDePertenencia.CriterioDePertenencia;
import entities.fuentes.Importador;
import entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Coleccion {
    private String titulo;
    private String descripcion;
    private Importador importador;
    private Map<String, Hecho> hechos;
    @Setter
    private List<CriterioDePertenencia> criteriosDePertenencia = new ArrayList<>();

    public Coleccion(String titulo, String descripcion, Importador importador) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.importador = importador;
        this.hechos = this.importador.obtenerHechos();
    }


    public void filtrarHechos(Map<String, Hecho> hechos1) {
        Map<String, Hecho> hechosFiltrados = new HashMap<>();
        for (Hecho hecho : hechos1.values()) {
            boolean cumpleTodosCriterios = true;

            for (CriterioDePertenencia criterio : criteriosDePertenencia) {
                if (!criterio.cumpleCriterio(hecho)) {
                    cumpleTodosCriterios = false;
                    break;
                }
            }

            if (cumpleTodosCriterios) {
                hechosFiltrados.put(hecho.getDatosHechos().getTitulo(), hecho);
            }
        }

        this.hechos = hechosFiltrados;
    }

    public void setCriteriosDePertenencia(List<CriterioDePertenencia> criterios) {
        criteriosDePertenencia.addAll(criterios);
        this.filtrarHechos(importador.obtenerHechos());
    }

    public void addHecho(Hecho hecho) {
        if(hecho.getEsValido()) {
            this.hechos.put(hecho.getDatosHechos().getTitulo(), hecho);
        }else{
            throw new RuntimeException("Hecho no es valido");
        }
    }

}
