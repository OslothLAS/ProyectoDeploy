//esta clase va a tomar el rol de un ABM con el sentido de cumplir los
//requerimientos a cumplir del visualizador

package entities.hechos;

import entities.colecciones.Coleccion;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositorioDeHechos {
    //por ahora creo el atributo hechos porque no se donde se guardan los hechos subidos
    Map<String, Hecho> hechos;

    public void subirHecho(String titulo, String descripcion, String categoria, String latitud, String longitud, LocalDate fechaHecho){
        Ubicacion ubi = new Ubicacion(latitud,longitud);
        DatosHechos data = new DatosHechos(titulo,descripcion, categoria, ubi, fechaHecho,LocalDate.now(), Origen.CARGA_MANUAL);
        Hecho hecho1 = Hecho.create(data,"MiNombre");
        //TODO (que hacer con el nombre del visualizador aca?
        hechos.put(titulo,hecho1);
    }

    /*  Los hechos son de tipo "Map" pero los paso a "List" para que sea mas fácil recorrerlos
    a la hora de visualizarlos              */
    public List<Hecho> visualizarHechosDeColeccion(Coleccion coleccion){
        Map<String, Hecho> hechos = coleccion.getHechos();

        return new ArrayList<>(hechos.values());
    }

    public List<Hecho> visualizarHechosConFiltro(Coleccion coleccion, String filtro) {
        String filtroNormalizado = normalizarTexto(filtro);

        return coleccion.getHechos().values().stream()
                .filter(hecho -> normalizarTexto(hecho.toString()).contains(filtroNormalizado))
                .collect(Collectors.toList());
    }


    private String normalizarTexto(String texto) {
        // SACA TILDES Y MAYUSCULAS
        String textoSinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoSinTildes.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

}
