//esta clase va a tomar el rol de un ABM con el sentido de cumplir los
//requerimientos a cumplir del visualizador

package entities.hechos;

import entities.colecciones.Coleccion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.ExtensionReader.normalizarTexto;

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


    public List<Hecho> visualizarHechosConFiltro(Coleccion coleccion, Map<String, String> filtrosAplicados) {
        return coleccion.getHechos().values().stream()
                .filter(hecho -> {
                    for (Map.Entry<String, String> filtro : filtrosAplicados.entrySet()) {
                        String campo = normalizarTexto(filtro.getKey());
                        String valorEsperado = normalizarTexto(filtro.getValue());

                        String valorReal = switch (campo) {
                            case "titulo" -> hecho.getDatosHechos().getTitulo();
                            case "descripcion" -> hecho.getDatosHechos().getDescripcion();
                            case "categoria" -> hecho.getDatosHechos().getCategoria();
                            case "fecha" -> hecho.getDatosHechos().getFechaHecho().toString();
                            default -> "";
                        };

                        if (!normalizarTexto(valorReal).contains(valorEsperado) || !hecho.getEsValido()) {
                            return false; // Si un filtro no se cumple, chau
                        }
                    }
                    return true; // Se cumplieron todos los filtros
                })
                .collect(Collectors.toList());
    }

}
