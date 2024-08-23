package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
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

    public CartItem findCartItemById(UUID cartItemId) {
        Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
        return cartItemOptional.orElseThrow(() -> 
            new RecordNotFoundException("CartItem with ID '" + cartItemId + "' not found"));
    }

    public CartItem updateCartItem(UUID cartItemId, CartItem updatedCartItem) {
        if (!cartItemRepo.existsById(cartItemId)) {
            throw new IllegalArgumentException("CartItem not found");
        }
        updatedCartItem.setCartItemId(cartItemId);
        return cartItemRepo.save(updatedCartItem);
    }

    public void deleteCartItem(UUID cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }
}
