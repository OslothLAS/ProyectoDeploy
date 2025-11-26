package ar.utn.ba.ddsi.fuenteDinamica.filters;

import ar.utn.ba.ddsi.fuenteDinamica.services.RateLimitingService;
import io.github.bucket4j.ConsumptionProbe; // Importante
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getRemoteAddr();
        io.github.bucket4j.Bucket tokenBucket = rateLimitingService.resolveBucket(apiKey);

        // Usamos 'tryConsumeAndReturnRemaining' para obtener detalles (Probe)
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Pasó: agregamos headers informativos (opcional)
            response.addHeader("X-Rate-Limit-Remaining", Long.toString(probe.getRemainingTokens()));
            return true;
        } else {
            // Bloqueado: Calculamos cuánto esperar
            long waitForRefillNanos = probe.getNanosToWaitForRefill();
            long waitForRefillSeconds = TimeUnit.NANOSECONDS.toSeconds(waitForRefillNanos);

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            // Header estándar HTTP para indicar tiempo de espera
            response.addHeader("Retry-After", String.valueOf(waitForRefillSeconds));
            response.setContentType("application/json");
            response.getWriter().write("{\"mensaje\": \"Has excedido el límite de peticiones.\"}");

            return false;
        }
    }
}