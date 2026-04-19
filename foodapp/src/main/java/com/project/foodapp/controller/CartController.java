package com.project.foodapp.controller;

import com.project.foodapp.entity.Cart;
import com.project.foodapp.entity.User;
import com.project.foodapp.repository.UserRepository;
import com.project.foodapp.service.CartService;
import com.project.foodapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // ✅ Helper method (VERY IMPORTANT)
    private Long getUserIdFromToken(HttpServletRequest request) {

        //it prevents from crash if the header is missing
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid or missing token");
        }

        String token = header.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }
    @GetMapping
    public Cart getCart(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    public Cart addItem(
            HttpServletRequest request,
            @RequestParam Long foodItemId,
            @RequestParam int quantity) {

        Long userId = getUserIdFromToken(request);
        return cartService.addItemToCart(userId, foodItemId, quantity);
    }

    @DeleteMapping("/remove")
    public Cart removeItem(
            HttpServletRequest request,
            @RequestParam Long cartItemId) {

        Long userId = getUserIdFromToken(request);

        return cartService.removeItemFromCart(userId, cartItemId);
    }

    @DeleteMapping("/clear")
    public Cart clearCart(HttpServletRequest request) {

        Long userId = getUserIdFromToken(request);

        return cartService.clearCart(userId);
    }
}