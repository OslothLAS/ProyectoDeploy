package services.impl;

import entities.colecciones.Coleccion;
import models.entities.criteriosDePertenencia.CriterioDePertenencia;
import models.entities.fuentes.Importador;
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


    //En este caso la idea es tomar los repositorios de las fuentes
    //y sus hechos,y agregarlos al repositorio del agregador
    //en este caso falta agregar el findAll a las fuentes

    public void actualizarHechos(IHechoRepository repositorioDeFuente){
        List<Hecho> hechos =  repositorioDeFuente.findAll();
        this.filtrarHechosValidos(hechos)
            .forEach
            (hecho -> this.hechoRepository.save(hecho));
    }

    private List<Hecho> filtrarHechosValidos(List<Hecho> hechos){
       return hechos.stream().filter(
               hecho -> hecho.getEsValido()).
               collect(Collectors.toList());
    }

    public Coleccion getColeccion(String titulo, String descripcion, List<Importador> importadores, List<CriterioDePertenencia> criteriosDePertenencia){
        List<Hecho> hechosSegunCriterio = this.hechoRepository.findSegunCriterios(criteriosDePertenencia);

        Coleccion coleccion = new Coleccion(titulo,descripcion,importadores,criteriosDePertenencia);

        //Agrego la coleccion a cada hecho
        hechosSegunCriterio.forEach(hecho -> {
            hecho.addColeccion(coleccion);
        });

        return coleccion;
    }

/* Capaz en algun momento use esta funcion
    private boolean perteneceALaColeccion(Hecho hecho, Coleccion coleccion){
        return hecho
                .getColecciones()
                .stream()
                .anyMatch(num -> num.equals(coleccion) );
    }
*/

}
