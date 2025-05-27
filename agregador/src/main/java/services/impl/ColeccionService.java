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

    private IHechoRepository hechoRepository;

    public ColeccionService(IHechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }


    public void actualizarHechos(){
        List<Hecho> hechos = filtrarHechosValidos(hechoRepository.findAll());
        this.actualizarColeccionesDeHechos(this.traerColecciones());
    }

    //Crea una lista con todas las colecciones existentes para usarse (por el momento)
    //en actualizarHechos
    public List<Coleccion> traerColecciones(){
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
        hechoRepository.findAll().forEach(hecho -> hechoRepository.save(hecho));
    }


}
