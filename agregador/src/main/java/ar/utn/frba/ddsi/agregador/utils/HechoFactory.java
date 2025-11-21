package ar.utn.frba.ddsi.agregador.utils;

import ar.utn.frba.ddsi.agregador.models.entities.hechos.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Factory para encapsular la lógica de creación de objetos Hecho.
 * Esto simplifica la creación de datos de prueba o la inicialización.
 */
public class HechoFactory {

    /**
     * Crea y devuelve una instancia completa y válida de un Hecho con datos de prueba.
     * @return Un objeto Hecho listo para ser guardado.
     */
    public static Hecho crearHechoDePrueba() {
        // --- Toda la lógica de creación está ahora aquí dentro ---
        Provincia provincia = new Provincia();
        provincia.setNombre("Buenos Aires");

        Localidad localidad = new Localidad();
        localidad.setNombre("Loma Hermosa");
        localidad.setProvincia(provincia);

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud("-34.5884");
        ubicacion.setLongitud("-58.5942");
        ubicacion.setLocalidad(localidad);

        Categoria categoria = new Categoria();
        categoria.setCategoria("Incidente Vial");

        List<Multimedia> multimediaList = new ArrayList<>();
        multimediaList.add(new Multimedia());

        // --- Usamos el Builder para construir el objeto final ---
        return Hecho.builder()
                .username("reportero_ciudadano")
                .esValido(true)
                .titulo("Fuerte colisión en Av. General Paz")
                .descripcion("Un choque múltiple entre tres vehículos particulares ocurrió esta tarde.")
                .categoria(categoria)
                .ubicacion(ubicacion)
                .fechaHecho(LocalDateTime.now().minusHours(2))
                .multimedia(multimediaList)
                .etiquetas(List.of("choque", "tráfico", "general paz"))
                .origen(Origen.DATASET)
                .fuenteOrigen(FuenteOrigen.DESCONOCIDO)
                .fechaCreacion(LocalDateTime.now())
                .esEditable(true)
                .build();
    }

    /**
     * Un método de ejemplo que permite personalizar un campo.
     * Puedes crear tantos métodos como necesites para diferentes escenarios de prueba.
     */
    public static Hecho crearHechoDePruebaConTitulo(String titulo) {
        Hecho hecho = crearHechoDePrueba();
        hecho.setTitulo(titulo);
        return hecho;
    }
}