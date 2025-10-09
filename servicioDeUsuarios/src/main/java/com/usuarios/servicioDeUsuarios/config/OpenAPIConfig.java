package com.usuarios.servicioDeUsuarios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        // 1. Información General de la API
        info = @Info(
                title = "Servicio de Usuarios (JWT Secured)",
                version = "0.0.1",
                description = "Documentación de la API para la gestión de usuarios, protegida con JWT.",
                contact = @Contact(
                        name = "Soporte Técnico",
                        email = "soporte@ejemplo.com"
                )
        ),
        // 3. Aplicar Requisito de Seguridad Global (Opcional)
        // Esto aplica el esquema 'bearerAuth' a TODA la API por defecto.
        // Si solo algunos endpoints son seguros, omite esta línea y usa @SecurityRequirement en el Controller.
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        // 2. Definición del Esquema de Seguridad JWT
        name = "bearer", // **Nombre de Referencia** que se usará para aplicar el requisito.
        type = SecuritySchemeType.HTTP,
        scheme = "bearer", // El tipo de esquema de autenticación HTTP.
        bearerFormat = "JWT", // Especifica el formato del token.
        description = "El token JWT debe incluirse en el encabezado Authorization."
)
public class OpenAPIConfig {
    // No se necesita código aquí, las anotaciones hacen el trabajo.
}