package com.vann.model.service;

import com.vann.model.ShoppingCartItem;
import com.vann.repositories.ShoppingCartItemRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingCartItemService {

    private final ShoppingCartItemRepo shoppingCartItemRepo;

    public ShoppingCartItemService(ShoppingCartItemRepo shoppingCartItemRepo) {
        this.shoppingCartItemRepo = shoppingCartItemRepo;
    }

    public ShoppingCartItem saveShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        return shoppingCartItemRepo.save(shoppingCartItem);
    }

    public Optional<ShoppingCartItem> findShoppingCartItemById(UUID shoppingCartItemUuid) {
        return shoppingCartItemRepo.findById(shoppingCartItemUuid);
    }

    public ShoppingCartItem updateShoppingCartItem(UUID shoppingCartItemUuid, ShoppingCartItem updatedShoppingCartItem) {
        // Check if the ShoppingCartItem exists
        if (!shoppingCartItemRepo.existsById(shoppingCartItemUuid)) {
            throw new IllegalArgumentException("ShoppingCartItem not found");
        }
        // Set the ID of the updated ShoppingCartItem
        updatedShoppingCartItem.setShoppingCartItemUuid(shoppingCartItemUuid);
        // Save the updated ShoppingCartItem
        return shoppingCartItemRepo.save(updatedShoppingCartItem);
    }

    public void deleteShoppingCartItem(UUID shoppingCartItemUuid) {
        shoppingCartItemRepo.deleteById(shoppingCartItemUuid);
    }
}
