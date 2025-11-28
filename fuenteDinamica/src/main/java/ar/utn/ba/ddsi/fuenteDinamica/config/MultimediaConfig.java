package ar.utn.ba.ddsi.fuenteDinamica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MultimediaConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String userHome = System.getProperty("user.home");

        Path uploadDir = Paths.get(userHome, "Pictures", "DDSI_Uploads");

        String uploadPath = uploadDir.toUri().toString();

        System.out.println(">>> [WebConfig] Mapeando recursos /uploads/** hacia: " + uploadPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowedMethods("*");
    }
}