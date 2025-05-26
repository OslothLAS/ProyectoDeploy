package services.impl;

import models.entities.colecciones.Coleccion;
import models.entities.hechos.Hecho;
import models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.IColeccionService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IHechoRepository hechoRepository;


    //No se de donde podemos sacar la lista de las colecciones que vamos a actualizar
    //asique por el momento agregue una lista de colecciones random, que vamos a sacar
    //de algun lado (osmar)

    public void actualizarHechos(IHechoRepository repositorioDeFuente, List<Coleccion> colecciones){
        List<Hecho> hechos = filtrarHechosValidos(repositorioDeFuente.findAll());
        this.actualizarColeccionesDeHecho(colecciones);
    }

    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(
               hecho -> hecho.getEsValido()).
               collect(Collectors.toList());
    }

    private void actualizarColeccionesDeHecho(List<Coleccion> colecciones){
        List<Hecho> hechos =  this.hechoRepository.findAll();
        colecciones.forEach(coleccion -> coleccion.filtrarHechos(hechos));
    }


/*
    public Coleccion getColeccion(String titulo, String descripcion, List<Importador> importadores, List<CriterioDePertenencia> criteriosDePertenencia){

        List<Hecho> hechosSegunCriterio = this.hechoRepository.findSegunCriterios(criteriosDePertenencia);
        Coleccion coleccion = new Coleccion(titulo,descripcion, importadores, criteriosDePertenencia);

        //Agrego la coleccion a cada hecho
        hechosSegunCriterio.forEach(hecho -> {
            hecho.addColeccion(coleccion);
        });

        return coleccion;
    }
*/

/* Capaz en algun momento use esta funcion
    private boolean perteneceALaColeccion(Hecho hecho, Coleccion coleccion){
        return hecho
                .getColecciones()
                .stream()
                .anyMatch(num -> num.equals(coleccion) );
    }
*/

}
