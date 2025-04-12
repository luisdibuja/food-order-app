package com.example.foodorder.controller;

import com.example.foodorder.model.Order;
import com.example.foodorder.service.OrderService;
import org.slf4j.Logger; // Importa Logger
import org.slf4j.LoggerFactory; // Importa LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Anotaciones MVC para REST

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Combina @Controller y @ResponseBody, ideal para APIs REST
//@RequestMapping("/api") // Ruta base para todos los endpoints en este controlador
public class OrderController {

    // Logger para registrar información y errores
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired // Inyecta el servicio de órdenes
    private OrderService orderService;

    // Endpoint para obtener la lista de platillos permitidos
    @GetMapping("/dishes")
    public ResponseEntity<List<String>> getAllowedDishes() {
        log.info("GET /api/dishes - Solicitando lista de platillos permitidos");
        return ResponseEntity.ok(orderService.getAllowedDishes());
    }

    // Endpoint para crear una nueva orden (POST /api/orders)
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Order order) { // @RequestBody mapea el JSON a un objeto Order
        log.info("POST /api/orders - Intentando crear pedido: {}", order);
        try {
            // Crea una nueva instancia para asegurar que no se usen IDs preexistentes del request
            Order orderToCreate = new Order();
            orderToCreate.setCustomerName(order.getCustomerName());
            orderToCreate.setItem(order.getItem());
            orderToCreate.setQuantity(order.getQuantity());
            // El estado se asigna en el servicio

            Order createdOrder = orderService.createOrder(orderToCreate);
            log.info("Pedido creado exitosamente con ID: {}", createdOrder.getId());
            // Devuelve 201 Created con el objeto creado en el cuerpo
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (IllegalArgumentException e) {
            // Error de validación de negocio (ej. platillo no válido)
            log.warn("Error de validación al crear pedido: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // Devuelve 400 Bad Request
        } catch (Exception e) {
            // Otros errores inesperados durante la creación
            log.error("Error interno al crear pedido: {}", order, e); // Loguea la excepción completa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al procesar el pedido.")); // Devuelve 500
        }
    }

    // Endpoint para obtener todas las órdenes (GET /api/orders)
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /api/orders - Solicitando todos los pedidos");
        List<Order> orders = orderService.getAllOrders();
        log.info("Devolviendo {} pedidos", orders.size());
        return ResponseEntity.ok(orders); // Devuelve 200 OK con la lista
    }

    // Endpoint para obtener una orden por su ID (GET /api/orders/{id})
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) { // @PathVariable extrae el ID de la URL
        log.info("GET /api/orders/{} - Solicitando pedido por ID", id);
        Optional<Order> order = orderService.getOrderById(id);
        // Forma funcional de devolver 200 OK si existe, o 404 Not Found si no
        return order.map(o -> {
                    log.info("Pedido encontrado: {}", o);
                    return ResponseEntity.ok(o);
                })
                .orElseGet(() -> {
                    log.warn("Pedido con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Endpoint para actualizar el estado de una orden (PUT /api/orders/{id}/status)
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status"); // Extrae el nuevo estado del cuerpo JSON ({ "status": "NUEVO_ESTADO" })
        log.info("PUT /api/orders/{}/status - Intentando actualizar estado a: {}", id, status);

        if (status == null || status.trim().isEmpty()) {
            log.warn("Intento de actualización de estado para ID {} fallido: estado no proporcionado.", id);
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'status' es requerido en el cuerpo de la petición."));
        }

        try {
            Optional<Order> updatedOrder = orderService.updateOrderStatus(id, status);
            // Devuelve 200 OK con la orden actualizada si se encontró, o 404 Not Found si no
            return updatedOrder
                    .map(o -> {
                        log.info("Estado del pedido {} actualizado a {}", id, status);
                        return ResponseEntity.ok(o);
                    })
                    .orElseGet(() -> {
                        log.warn("Pedido con ID {} no encontrado para actualizar estado.", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            // Captura errores inesperados durante la actualización
            log.error("Error interno al actualizar estado del pedido {}: {}", id, status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor al actualizar el estado del pedido."));
        }
    }
}
