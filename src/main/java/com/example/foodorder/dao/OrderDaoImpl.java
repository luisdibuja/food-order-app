package com.example.foodorder.dao;

import com.example.foodorder.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query; // Importa Query de Hibernate
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Marca como un componente DAO gestionado por Spring
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private SessionFactory sessionFactory; // Inyecta el SessionFactory configurado en Spring

    // Método helper para obtener la sesión de Hibernate actual gestionada por Spring
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Order save(Order order) {
        // Persiste o actualiza la entidad. merge() es generalmente preferido a saveOrUpdate().
        // Devuelve la instancia gestionada por la sesión.
        return getCurrentSession().merge(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        // Usa get() para obtener la entidad por ID. Devuelve null si no se encuentra.
        Order order = getCurrentSession().get(Order.class, id);
        // Envuelve el resultado en un Optional.
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {
        // Crea una consulta HQL (Hibernate Query Language) para obtener todas las órdenes.
        // Se especifica la clase Order.class para obtener resultados tipados.
        // Se ordena por ID descendente para mostrar las más recientes primero.
        Query<Order> query = getCurrentSession().createQuery("FROM Order o ORDER BY o.id DESC", Order.class);
        // Ejecuta la consulta y devuelve la lista de resultados.
        return query.getResultList();
    }
}
