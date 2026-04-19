package com.project.foodapp.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private Double totalPrice;

    @ManyToOne
    @JsonIgnore
    private FoodItem foodItem;

    @ManyToOne
    @JsonIgnore
    private Cart cart;
}