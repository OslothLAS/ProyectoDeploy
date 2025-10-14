package ar.utn.frba.ddsi.agregador;


import ar.utn.frba.ddsi.agregador.models.entities.normalizador.OpenStreetMap;
import ar.utn.frba.ddsi.agregador.services.IColeccionService;
import ar.utn.frba.ddsi.agregador.services.impl.ColeccionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;

@SpringBootApplication
public class AgregadorApplication {


	public static void main(String[] args) {
		SpringApplication.run(AgregadorApplication.class, args);

		OpenStreetMap osm = new OpenStreetMap();

		// Ejemplo 1: Obelisco de Buenos Aires
		double lat1 = -34.914536;
		double lon1 = -060.035774;
		List<String> resultado1 = osm.obtenerUbicacion(lat1, lon1);
		System.out.println("Chivilcoy en alerta por Emanación de gas tóxico");
		System.out.println("Buenos Aires -> " + resultado1.get(0));
		System.out.println("Chivilcoy -> " + resultado1.get(1));


	}





}
