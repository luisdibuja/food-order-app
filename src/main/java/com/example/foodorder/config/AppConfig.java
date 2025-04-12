package com.example.foodorder.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "com.example.foodorder",
        excludeFilters = {
                // Excluimos la configuración MVC (@EnableWebMvc) para que WebConfig la maneje exclusivamente
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
public class AppConfig {
    // Esta clase configura el escaneo de componentes para el contexto raíz (servicios, DAOs, etc.)
    // Puede estar vacía si toda la configuración está delegada a otras clases @Configuration
    // que son escaneadas por el basePackage.
}
