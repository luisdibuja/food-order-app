package com.example.foodorder.config; // O com.example.foodorder.filter

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Puedes usar la anotación @WebFilter si tu contenedor la soporta y escanea,
// pero registrarlo en web.xml es más explícito y tradicional.
// @WebFilter("/api/*")
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Método de inicialización del filtro (opcional)
        // Puedes leer parámetros de configuración desde web.xml si los defines
        System.out.println("CorsFilter inicializado!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Añade las cabeceras CORS a CADA respuesta que pase por este filtro
        // Permite cualquier origen (ajustar en producción si es necesario)
        response.setHeader("Access-Control-Allow-Origin", "*");
        // Permite los métodos HTTP estándar
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // Permite cabeceras comunes (incluyendo Content-Type y cabeceras personalizadas como Authorization)
        response.setHeader("Access-Control-Allow-Headers", "*");
        // Opcional: Tiempo que el navegador puede cachear la respuesta preflight (en segundos)
        // response.setHeader("Access-Control-Max-Age", "3600");

        // Manejo especial para peticiones OPTIONS (preflight)
        // El navegador envía una petición OPTIONS antes de peticiones "complejas" (como PUT, DELETE o con ciertas cabeceras)
        // para verificar los permisos CORS. Debemos responder OK a estas peticiones.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // Responde 200 OK
        } else {
            // Para todas las demás peticiones (GET, POST, etc.), simplemente
            // continúa con la cadena de filtros (que eventualmente llegará al DispatcherServlet de Spring)
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Método de destrucción del filtro (opcional)
        System.out.println("CorsFilter destruido.");
    }
}
