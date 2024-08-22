package com.vann.service;

import com.vann.model.CartItem;
import com.vann.repositories.CartItemRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {

    private final CartItemRepo cartItemRepo;

    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }

    public Optional<CartItem> findCartItemById(UUID cartItemId) {
        return cartItemRepo.findById(cartItemId);
    }

    public CartItem updateCartItem(UUID cartItemId, CartItem updatedCartItem) {
        // Check if the CartItem exists
        if (!cartItemRepo.existsById(cartItemId)) {
            throw new IllegalArgumentException("CartItem not found");
        }
        // Set the ID of the updated CartItem
        updatedCartItem.setCartItemId(cartItemId);
        // Save the updated CartItem
        return cartItemRepo.save(updatedCartItem);
    }

    public void deleteCartItem(UUID cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }
}
