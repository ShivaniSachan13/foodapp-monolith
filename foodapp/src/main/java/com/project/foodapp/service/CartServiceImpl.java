package com.project.foodapp.service;

import com.project.foodapp.entity.*;
import com.project.foodapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    // ✅ GET CART
    @Override
    public Cart getCartByUserId(Long userId) {

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {

                    // create cart if not exists
                    Cart cart = new Cart();
                    cart.setUser(userRepository.findById(userId).get());

                    return cartRepository.save(cart);
                });
    }

    // ✅ ADD ITEM
    @Override
    public Cart addItemToCart(Long userId, Long foodItemId, int quantity) {

        Cart cart = getCartByUserId(userId);

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        // 🔥 STEP 1: check if item already exists
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getFoodItem().getId().equals(foodItemId))
                .findFirst();

        if (existingItem.isPresent()) {

            // ✅ UPDATE EXISTING ITEM
            CartItem item = existingItem.get();

            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalPrice(item.getQuantity() * foodItem.getPrice());

            cartItemRepository.save(item);

        } else {

            // ✅ CREATE NEW ITEM
            CartItem newItem = new CartItem();
            newItem.setFoodItem(foodItem);
            newItem.setQuantity(quantity);
            newItem.setTotalPrice(foodItem.getPrice() * quantity);
            newItem.setCart(cart);

            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        // 🔥 RECALCULATE TOTAL PRICE
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        cart.setTotalPrice(total);

        return cartRepository.save(cart);
    }

    // ✅ REMOVE ITEM
    @Override
    public Cart removeItemFromCart(Long userId, Long cartItemId) {

        Cart cart = getCartByUserId(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // update price
        cart.setTotalPrice(cart.getTotalPrice() - item.getTotalPrice());

        // remove item
        cart.getItems().remove(item);

        cartItemRepository.delete(item);

        return cartRepository.save(cart);
    }

    // ✅ CLEAR CART
    @Override
    public Cart clearCart(Long userId) {

        Cart cart = getCartByUserId(userId);

        cart.getItems().clear();
        cart.setTotalPrice(0.0);

        return cartRepository.save(cart);
    }
}
