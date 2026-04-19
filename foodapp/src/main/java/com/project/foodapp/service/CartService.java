package com.project.foodapp.service;

import com.project.foodapp.entity.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);

    Cart addItemToCart(Long userId, Long foodItemId, int quantity);

    Cart removeItemFromCart(Long userId, Long cartItemId);

    Cart clearCart(Long userId);
}