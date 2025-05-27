package services.impl;

import models.entities.colecciones.Coleccion;
import models.entities.hechos.Hecho;
import models.repositories.IHechoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.IColeccionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IHechoRepository hechoRepository;


    public void actualizarHechos(){
        List<Hecho> hechos = filtrarHechosValidos(hechoRepository.findAll());
        this.actualizarColeccionesDeHechos(this.traerColecciones());
    }

    //Crea una lista con todas las colecciones existentes para usarse (por el momento)
    //en actualizarHechos
    private List<Coleccion> traerColecciones(){
        Set<Coleccion> colecciones = new HashSet<>();
        List<Hecho> hechos = hechoRepository.findAll();
        hechos.forEach(hecho -> colecciones.addAll(hecho.getColecciones()));
        return new ArrayList<>(colecciones);
    }

    //Esto es solo para agregar los hechos validos
    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(
               hecho -> hecho.getEsValido()).
               collect(Collectors.toList());
    }

    private void actualizarColeccionesDeHechos(List<Coleccion> colecciones){
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
