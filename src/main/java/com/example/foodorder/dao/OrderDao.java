package com.example.foodorder.dao;

import com.example.foodorder.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order order); // Guarda o actualiza una orden
    Optional<Order> findById(Long id); // Busca una orden por su ID
    List<Order> findAll(); // Obtiene todas las órdenes
    // No se necesita un método 'update' explícito si se usa la gestión de sesión de Hibernate
    // y @Transactional en el servicio.
}
