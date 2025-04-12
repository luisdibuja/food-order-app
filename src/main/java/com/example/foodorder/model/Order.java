package com.example.foodorder.model;

import jakarta.persistence.*; // Importa las anotaciones de Jakarta Persistence (JPA)

@Entity // Marca esta clase como una entidad JPA
@Table(name = "orders") // Mapea esta entidad a la tabla "orders" en la base de datos
public class Order {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID (IDENTITY para PostgreSQL SERIAL/BIGSERIAL)
    private Long id;

    @Column(name = "customer_name", nullable = false) // Mapea al campo 'customer_name', no puede ser nulo
    private String customerName;

    @Column(nullable = false, length = 100) // Mapea al campo 'item', no nulo, longitud máxima 100
    private String item;

    @Column(nullable = false) // Mapea al campo 'quantity', no nulo
    private int quantity;

    @Column(nullable = false, length = 50) // Mapea al campo 'status', no nulo, longitud máxima 50
    private String status; // e.g., "PENDING", "PROCESSING", "COMPLETED"

    // Constructor vacío requerido por JPA/Hibernate
    public Order() {}

    // Constructor útil (sin ID, ya que se genera automáticamente)
    public Order(String customerName, String item, int quantity) {
        this.customerName = customerName;
        this.item = item;
        this.quantity = quantity;
        this.status = "PENDING"; // Estado inicial por defecto
    }

    // Getters y Setters (necesarios para JPA/Hibernate y manejo de datos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String name) { this.customerName = name; }
    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}
