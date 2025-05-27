package services.impl;

import models.entities.colecciones.Coleccion;
import models.entities.criteriosDePertenencia.CriterioDePertenencia;
import models.entities.criteriosDePertenencia.CriterioPorCategoria;
import models.entities.fuentes.Importador;
import models.entities.fuentes.ImportadorHechos;
import models.entities.hechos.DatosHechos;
import models.entities.hechos.Hecho;
import models.entities.hechos.Ubicacion;
import models.entities.usuarios.Administrador;
import models.entities.usuarios.Usuario;
import models.repositories.impl.HechoMemoryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionServiceTest {
        @Test
        public void actualizarColeccionesDelRepositorio() {

            HechoMemoryRepository repo = new HechoMemoryRepository();

            Importador importador = null;
            List<Importador> importadores = new ArrayList<>();
            importadores.add(importador);

            CriterioPorCategoria criterio1 = new CriterioPorCategoria("Inundaciones");
            CriterioPorCategoria criterio2 = new CriterioPorCategoria("Incendios");

            
            List<CriterioDePertenencia> criterios = new ArrayList<>();
            criterios.add(criterio1);

            List<CriterioDePertenencia> criterios1 = new ArrayList<>();
            //criterios1.add(criterio1);
            criterios1.add(criterio2);

            Coleccion c1 = new Coleccion("Inundacion Bahia", "Inundacion en bahia", importadores,  criterios);
            Coleccion c2 = new Coleccion("Incendios Norte", "Incendios en el norte del pais", importadores, criterios1);


            Administrador user = new Administrador(1L, "Osmar");
            Ubicacion ubi = new Ubicacion("1","2");
            DatosHechos datosHecho1 = new DatosHechos("A", "Inundaciones","Inundaciones",ubi, LocalDate.now());
            DatosHechos datosHecho2 = new DatosHechos("B", "Incendio en mi casa","Incendios",ubi, LocalDate.now());
            DatosHechos datosHecho3 = new DatosHechos("B", "Incendio en mi casa","Incendios e Inundaciones",ubi, LocalDate.now());

            //Creo un hecho que contenga los dos hechos
            Hecho hecho3 = Hecho.create(datosHecho1, "Osmar");
            hecho3.addColeccion(c1);
            hecho3.addColeccion(c2);

            Hecho hecho4 = Hecho.create(datosHecho1, "Osmar");
            hecho3.addColeccion(c1);


            Hecho hecho1 = Hecho.create(datosHecho1, "Osmar");

            Hecho hecho2 = Hecho.create(datosHecho2, "Osmar");

            //Guardo todos los hechos creados en el repositorio general
            repo.save(hecho1);
            repo.save(hecho2);
            repo.save(hecho3);
            repo.save(hecho4);

            ColeccionService coleccionService = new ColeccionService(repo);

            //ESTOS SON LOS HECHOS SIN ACTUALIZAR, DEBERIAN ESTAR EN NULAS LAS COLECCIONES QUE TIENEN
            assertEquals(hecho1.getColecciones(), List.of());
            assertEquals(hecho2.getColecciones(), List.of());

            //ACTUALIZAMOS LOS HECHOS PARA AGREGAR LAS COLECCIONES A SUS RESPECTIVOS HECHOS
            //Recordemos que estos hechos residen en el repositorio de hechos
            coleccionService.actualizarHechos();

            //ESTOS SON LOS HECHOS ACTUALIZADOS CON LAS COLECCIONES QUE YA EXISTIAN
            assertEquals(hecho1.getColecciones(), List.of(c1));
            assertEquals(hecho2.getColecciones(), List.of(c2));
            assertEquals(coleccionService.traerColecciones(), List.of(c1,c2));


        }


}
