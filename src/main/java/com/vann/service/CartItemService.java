package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.CartItem;
import com.vann.repositories.CartItemRepo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CartItemService {

    private final CartItemRepo cartItemRepo;

    public CartItemService(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    public CartItem createCartItem(UUID productId, Integer quantity) {
        CartItem cartItem = new CartItem(productId, quantity);
        return saveCartItem(cartItem);
    }

    public List<CartItem> findAllCartItems() {
        return cartItemRepo.findAll();
    }

    public CartItem findCartItemById(UUID cartItemId) {
        Optional<CartItem> cartItemOptional = cartItemRepo.findById(cartItemId);
        return cartItemOptional.orElseThrow(() -> 
            new RecordNotFoundException("CartItem with ID '" + cartItemId + "' not found"));
    }

    public CartItem updateCartItem(UUID cartItemId, CartItem updatedCartItem) {
        if (!cartItemRepo.existsById(cartItemId)) {
            throw new RecordNotFoundException("CartItem with id '" + cartItemId + "' not found");
        }
        updatedCartItem.setCartItemId(cartItemId);
        return saveCartItem(updatedCartItem);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }

    public void deleteCartItem(UUID cartItemId) {
        if (!cartItemRepo.existsById(cartItemId)) {
            throw new RecordNotFoundException("CartItem with id '" + cartItemId + "' not found");
        }
        cartItemRepo.deleteById(cartItemId);
    }
}
