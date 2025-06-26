package ar.utn.frba.ddsi.agregador.consenso.strategies;

import entities.colecciones.Fuente;
import entities.hechos.DatosHechos;
import entities.hechos.Hecho;
import entities.hechos.Origen;
import entities.hechos.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static entities.hechos.Origen.*;
import static org.junit.jupiter.api.Assertions.*;

class ConsensoMultipleMencionStrategyTest {

        ConsensoMultipleMencionStrategy consenso = new ConsensoMultipleMencionStrategy();

        @Test
        void testFiltrarHechosEnMultiplesFuentes() {


            // 1. Preparar datos de prueba
            Ubicacion ubicacion1 = new Ubicacion("Ciudad A", "País X");
            Ubicacion ubicacion2 = new Ubicacion("Ciudad B", "País Y");

            // Crear DatosHechos
            DatosHechos datos1 = new DatosHechos("Título 1", "Descripción A", "Categoría 1", ubicacion1, LocalDate.now());
            DatosHechos datos2 = new DatosHechos("Título 2", "Descripción B", "Categoría 2", ubicacion2, LocalDate.now());

            // Crear fuentes con diferentes orígenes
            Fuente fuente1 = new Fuente("192.168.1.1", "8080", DINAMICO);
            Fuente fuente2 = new Fuente("192.168.1.2", "8080", EXTERNO);
            Fuente fuente3 = new Fuente("192.168.1.3", "8080", ESTATICO);
            List<Fuente> fuentes = Arrays.asList(fuente1, fuente2, fuente3);

            // Crear hechos
            Hecho hecho1 = Hecho.create(datos1, DINAMICO);
            Hecho hecho2 = Hecho.create(datos1,EXTERNO); // Mismo DatosHechos, distinto origen
            Hecho hecho3 = Hecho.create(datos2, DINAMICO); // DatosHechos únicos
            Hecho hecho4 = Hecho.create(datos1, ESTATICO); // Tercera ocurrencia de datos1

            List<Hecho> hechos = Arrays.asList(hecho1, hecho2, hecho3, hecho4);

            // 2. Ejecutar el método a testear
            List<Hecho> resultado = consenso.obtenerHechosConsensuados(fuentes, hechos);

            // 3. Verificaciones
            assertEquals(1, resultado.size());

            // Verificar que el resultado contiene alguno de los hechos con datos1
            boolean contieneHechoCompartido = resultado.stream()
                    .anyMatch(h -> h.getDatosHechos().equals(datos1));
            assertTrue(contieneHechoCompartido);

            // Verificar que no contiene el hecho con datos únicos
            boolean contieneHechoUnico = resultado.stream()
                    .anyMatch(h -> h.getDatosHechos().equals(datos2));
            assertFalse(contieneHechoUnico);
        }

        @Test
        void testCuandoNoHayCoincidencias() {
            Ubicacion ubicacion = new Ubicacion("Ciudad C", "País Z");
            DatosHechos datosUnicos = new DatosHechos("Título Único", "Descripción Única", "Categoría Única",
                    ubicacion, LocalDate.now());

            Fuente fuente = new Fuente("localhost", "8080", DINAMICO);
            Hecho hecho = Hecho.create(datosUnicos, DINAMICO);

            List<Hecho> resultado = consenso.obtenerHechosConsensuados(List.of(fuente), List.of(hecho));

            assertTrue(resultado.isEmpty());
        }

        @Test
        void testConHechosNulos() {
            Ubicacion ubicacion = new Ubicacion("Ciudad D", "País W");
            DatosHechos datosValidos = new DatosHechos("Título Válido", "Descripción", "Categoría",
                    ubicacion, LocalDate.now());

            Fuente fuente1 = new Fuente("192.168.1.1", "8080", DINAMICO);
            Fuente fuente2 = new Fuente("192.168.1.2", "8080", EXTERNO);

            Hecho hechoValido1 = Hecho.create(datosValidos, DINAMICO);
            Hecho hechoValido2 = Hecho.create(datosValidos,EXTERNO);

            List<Hecho> hechos = Arrays.asList(hechoValido1, null, hechoValido2);

            List<Hecho> resultado = consenso.obtenerHechosConsensuados(
                    Arrays.asList(fuente1, fuente2), hechos);

            assertEquals(1, resultado.size());
        }


}