package com.project.foodapp.controller;

import com.project.foodapp.dto.OrderRequest;
import com.project.foodapp.entity.Order;
import com.project.foodapp.entity.OrderStatus;
import com.project.foodapp.response.ApiResponse;
import com.project.foodapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @RateLimiter(name = "orderLimiter", fallbackMethod = "rateLimitFallback")
    public ApiResponse<Order> placeOrder(
            @RequestBody OrderRequest request,
            Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();

        return new ApiResponse<>(
                true,
                "Order placed successfully",
                orderService.placeOrder(request, userEmail)
        );
    }

    //show me my orders
    @GetMapping
    public ApiResponse<List<Order>> getUserOrders(Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();

        return new ApiResponse<>(
                true,
                "Orders fetched successfully",
                orderService.getUserOrders(userEmail)
        );
    }

    //show all orders(admin dashboard)
    @GetMapping("/all")
    public ApiResponse<List<Order>> getAllOrders() {
        return new ApiResponse<>(
                true,
                "All orders fetched",
                orderService.getAllOrders()
        );
    }

    //Admin API
    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        return new ApiResponse<>(
                true,
                "Order status updated",
                orderService.updateStatus(id, status)
        );
    }

    //fallback method
    public ApiResponse<String> rateLimitFallback(
            OrderRequest request,
            Authentication authentication,
            Exception ex) {

        return new ApiResponse<>(
                false,
                "Too many requests! Please try again later.",
                null
        );
    }
}
