package com.project.foodapp.service;

import com.project.foodapp.dto.OrderRequest;
import com.project.foodapp.dto.OrderItemRequest;
import com.project.foodapp.entity.*;
import com.project.foodapp.repository.OrderRepository;
import com.project.foodapp.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.foodapp.entity.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    public Order placeOrder(OrderRequest request, String userEmail) {

        Order order = new Order();
        order.setUserEmail(userEmail);
        order.setStatus(OrderStatus.PLACED);
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            FoodItem foodItem = foodItemRepository.findById(itemRequest.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemRequest.getQuantity());

            double price = foodItem.getPrice() * itemRequest.getQuantity();
            orderItem.setPrice(price);

            orderItem.setOrder(order);

            total += price;
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    //so that user can see what orders have I placed
    public List<Order> getUserOrders(String userEmail) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getUserEmail().equals(userEmail))
                .toList();
    }

    //Admin needs to see all orders in a system
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    //order changes over time : placed -> out for delivery -> delivered
    public Order updateStatus(Long id, OrderStatus status) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}
