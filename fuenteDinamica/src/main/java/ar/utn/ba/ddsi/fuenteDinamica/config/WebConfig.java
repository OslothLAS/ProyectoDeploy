package ar.utn.ba.ddsi.fuenteDinamica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeo: URL Web -> Carpeta Física
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/DDSIMAGENES/");
    }

    // Bonus: Configuración de CORS para que el Front (8081) pueda pedir datos
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081") // Tu frontend
                .allowedMethods("*");
    }
}