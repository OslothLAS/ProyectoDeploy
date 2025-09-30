package ar.utn.frba.ddsi.agregador.services.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ColeccionServiceTest {
    //levantar todas las fuentes para correr los tests
/*
    @Test
    void consensuarHechos() {
        DatosHechos datos1 = new DatosHechos("titulo1","desc1","cat1",null, LocalDate.now());
        DatosHechos datos2 = new DatosHechos("titulo2","desc2","cat2",null, LocalDate.now());

        Hecho hecho1 = Hecho.create(datos1);
        Hecho hecho2 = Hecho.create(datos2);

        // Creamos una colección real, no un mock
        Coleccion coleccion = new Coleccion(
                "titulo",
                "desc",
                List.of(), // importadores
                List.of(), // criterios
                new MultipleMencion() // estrategia real
        );
        hecho1.addColeccion(coleccion.getHandle());
        hecho2.addColeccion(coleccion.getHandle());

        HechoMemoryRepository hechoRepository = mock(HechoMemoryRepository.class);
        ColeccionMemoryRepository coleccionRepository = mock(ColeccionMemoryRepository.class);

        when(coleccionRepository.findAll()).thenReturn(List.of(coleccion));
        when(hechoRepository.findAll()).thenReturn(List.of(hecho1, hecho2));

        hechoRepository.save(hecho1);
        hechoRepository.save(hecho2);

        System.out.println(hecho1.getColecciones());

        ColeccionService coleccionService = new ColeccionService(hechoRepository, coleccionRepository);

        when(coleccionRepository.findAll()).thenReturn(List.of(coleccion));

        coleccionService.consensuarHechos();

        List<Hecho> hechos = hechoRepository.findAll();
        System.out.println(hechos.get(0).getDatosHechos().getTitulo());
        System.out.println(hechos.get(0).getColecciones());

        Assertions.assertFalse(hechos.get(0).getEsConsensuado());
        Assertions.assertFalse(hechos.get(1).getEsConsensuado());
    }
*/

}