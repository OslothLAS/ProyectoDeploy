package com.usuarios.servicioDeUsuarios;

import com.usuarios.servicioDeUsuarios.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServicioDeUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioDeUsuariosApplication.class, args);
        JwtUtil.init();
	}

}
