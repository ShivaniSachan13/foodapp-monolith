package com.project.foodapp.entity;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    //this line prevents infinite loop JSON error
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Order order;

    // ✅ GETTERS

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public Order getOrder() {
        return order;
    }

    // ✅ SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}