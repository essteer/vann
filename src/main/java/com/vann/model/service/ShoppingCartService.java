package com.vann.model.service;

import com.vann.model.ShoppingCart;
import com.vann.repositories.ShoppingCartRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepo shoppingCartRepo;

    public ShoppingCartService(ShoppingCartRepo shoppingCartRepo) {
        this.shoppingCartRepo = shoppingCartRepo;
    }

    public ShoppingCart saveShoppingCart(ShoppingCart ShoppingCart) {
        return shoppingCartRepo.save(ShoppingCart);
    }

    public Optional<ShoppingCart> findShoppingCartById(UUID shoppingCartUuid) {
        return shoppingCartRepo.findById(shoppingCartUuid);
    }

    public ShoppingCart updateShoppingCart(UUID shoppingCartUuid, ShoppingCart updatedShoppingCart) {
        // Check if the ShoppingCart exists
        if (!shoppingCartRepo.existsById(shoppingCartUuid)) {
            throw new IllegalArgumentException("ShoppingCart not found");
        }
        // Set the ID of the updated ShoppingCart
        updatedShoppingCart.setShoppingCartUuid(shoppingCartUuid);
        // Save the updated ShoppingCart
        return shoppingCartRepo.save(updatedShoppingCart);
    }

    public void deleteShoppingCart(UUID shoppingCartUuid) {
        shoppingCartRepo.deleteById(shoppingCartUuid);
    }

}
