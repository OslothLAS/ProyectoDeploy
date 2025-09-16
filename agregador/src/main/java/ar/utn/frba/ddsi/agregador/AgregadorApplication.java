package ar.utn.frba.ddsi.agregador;

import entities.normalizador.GeoRefService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import entities.normalizador.OpenStreetMap;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = "entities")
public class AgregadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorApplication.class, args);


		OpenStreetMap osm = new OpenStreetMap();
		GeoRefService geo = new GeoRefService();

		// Ejemplo 1: Obelisco de Buenos Aires
		double lat1 = -34.914536;
		double lon1 = -060.035774;
		List<String> resultado1 = osm.obtenerUbicacion(lat1, lon1);
		System.out.println("Chivilcoy en alerta por Emanación de gas tóxico");
		System.out.println("Buenos Aires -> " + resultado1.get(0));
		System.out.println("Chivilcoy -> " + resultado1.get(1));


		List<String> resultado2 = geo.obtenerUbicacion(lat1, lon1);
		System.out.println("Provincia -> " + resultado1.get(0));
		System.out.println("Obelisco -> " + resultado1.get(1));


	}



}
