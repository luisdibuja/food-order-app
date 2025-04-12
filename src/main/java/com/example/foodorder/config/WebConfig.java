package com.example.foodorder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc // Habilita la configuración avanzada de Spring MVC
@ComponentScan(basePackages = "com.example.foodorder.controller") // Escanea solo el paquete de controladores
public class WebConfig implements WebMvcConfigurer {

    // Configura el convertidor de mensajes JSON para manejar @RequestBody y @ResponseBody
    // Asegura que se registren módulos necesarios como JavaTimeModule para fechas/horas Java 8+
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Registra el módulo para manejar tipos de fecha/hora de Java 8 (LocalDate, LocalDateTime, etc.)
        objectMapper.registerModule(new JavaTimeModule());
        // Puedes configurar más opciones del ObjectMapper aquí si es necesario
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    // Configura CORS globalmente para la API
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica CORS a todas las rutas bajo /api/
                .allowedOrigins("*") // Permite peticiones desde cualquier origen (¡restringir en producción!)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Headers permitidos en la petición
                .allowCredentials(false); // Cambiar a true si necesitas cookies/autenticación basada en sesión
    }

    // Aquí puedes añadir más configuraciones específicas de Web MVC si las necesitas:
    // - ViewResolvers (si usaras plantillas como Thymeleaf o JSP)
    // - Resource Handlers (para servir archivos estáticos como CSS, JS, imágenes)
    // - Interceptors
    // - Argument Resolvers
    // ...
}
