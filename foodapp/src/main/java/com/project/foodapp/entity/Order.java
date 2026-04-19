package com.project.foodapp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders") // "order" is reserved keyword in SQL
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail; // from JWT

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PLACED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //this line prevents infinite loop JSON error
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<OrderItem> items;

    // ✅ GETTERS

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // ✅ SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}