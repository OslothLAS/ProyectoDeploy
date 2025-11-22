package ar.utn.frba.ddsi.agregador.config;


import ar.utn.frba.ddsi.agregador.filters.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aca hay que poner las rutas que queremos limitar, en este caso todos los endpoints
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**");

    }
}