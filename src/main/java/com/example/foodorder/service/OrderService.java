package com.example.foodorder.service;

import com.example.foodorder.dao.OrderDao;
import com.example.foodorder.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Habilita transacciones declarativas

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service // Marca como un componente de servicio gestionado por Spring
public class OrderService {

    // Lista fija de platillos permitidos (Considerar cargar desde la BD en una versión futura)
    private static final List<String> ALLOWED_DISHES = Arrays.asList(
            "Pizza Margherita", "Hamburguesa Clásica", "Ensalada César", "Tacos al Pastor", "Sopa de Tortilla"
    );

    @Autowired // Inyecta la implementación del DAO
    private OrderDao orderDao;

    // Devuelve la lista de platillos permitidos (para el frontend)
    public List<String> getAllowedDishes() {
        return ALLOWED_DISHES;
    }

    // Valida si un platillo está en la lista permitida
    private boolean isDishAllowed(String dish) {
        return ALLOWED_DISHES.contains(dish);
    }

    // Método para crear una orden. @Transactional asegura que se ejecute en una transacción.
    @Transactional // Si ocurre una excepción Runtime, se hará rollback.
    public Order createOrder(Order order) throws IllegalArgumentException {
        // Validación de negocio: el platillo debe estar permitido
        if (!isDishAllowed(order.getItem())) {
            throw new IllegalArgumentException("Platillo no válido: " + order.getItem() +
                    ". Platillos permitidos: " + ALLOWED_DISHES);
        }
        // Validación de datos básicos
        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty() ||
                order.getItem() == null || order.getItem().isEmpty() || // Redundante por la validación anterior
                order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Datos del pedido incompletos o inválidos.");
        }

        order.setStatus("PENDING"); // Establece el estado inicial
        System.out.println("Guardando pedido vía DAO: " + order);
        return orderDao.save(order); // Delega el guardado al DAO
    }

    // Obtiene todas las órdenes. readOnly=true es una optimización para consultas.
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        System.out.println("Obteniendo todos los pedidos vía DAO.");
        return orderDao.findAll();
    }

    // Obtiene una orden por ID.
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        System.out.println("Buscando pedido con ID: " + id + " vía DAO.");
        return orderDao.findById(id);
    }

    // Actualiza el estado de una orden.
    @Transactional
    public Optional<Order> updateOrderStatus(Long id, String status) {
        System.out.println("Intentando actualizar estado del pedido ID: " + id + " a " + status + " vía DAO.");
        // Busca la orden existente
        Optional<Order> optionalOrder = orderDao.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status); // Modifica el estado del objeto persistente
            // Gracias a @Transactional y la gestión de sesión de Hibernate,
            // no es estrictamente necesario llamar a orderDao.save(order) aquí.
            // Hibernate detectará el cambio (dirty checking) y lo actualizará en la BD
            // al hacer commit de la transacción. Sin embargo, llamar a save/merge no haría daño.
            System.out.println("Estado del pedido " + id + " actualizado (Hibernate Dirty Checking).");
            return Optional.of(order); // Devuelve la orden actualizada
        } else {
            System.out.println("Pedido con ID: " + id + " no encontrado para actualizar estado.");
            return Optional.empty(); // Devuelve vacío si no se encontró
        }
    }
}
