package ar.utn.frba.ddsi.agregador.filters;


import ar.utn.frba.ddsi.agregador.services.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getRemoteAddr(); // O usar un header específico / JWT token

        // Consumir 1 token
        io.github.bucket4j.Bucket tokenBucket = rateLimitingService.resolveBucket(apiKey);

        if (tokenBucket.tryConsume(1)) {
            return true; // Tiene tokens, pasa
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Has excedido el limite de peticiones.");
            return false; // Bloqueado
        }
    }
}