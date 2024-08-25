package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Cart;
import com.vann.repositories.CartRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartService(CartRepo cartRepo, CustomerService customerService, ProductService productService) {
        this.cartRepo = cartRepo;
        this.customerService = customerService;
        this.productService = productService;
    }

    public List<Cart> findAllCarts() {
        return cartRepo.findAll();
    }

    public Cart findCartById(UUID cartId) {
        Optional<Cart> cartOptional = cartRepo.findById(cartId);
        return cartOptional.orElseThrow(() -> 
            new RecordNotFoundException("Cart with ID '" + cartId + "' not found"));
    }

    public Cart createOrFindCartByCustomerId(UUID customerId) throws RecordNotFoundException {
        customerService.findCustomerById(customerId);
        try {
            Cart cart = findCartByCustomerId(customerId);
            return cart;
        } catch (RecordNotFoundException e) {
            Cart newCart = new Cart(customerId, new HashMap<>());
            return cartRepo.save(newCart);
        }
    }

    private Cart findCartByCustomerId(UUID customerId) throws RecordNotFoundException {
        Optional<Cart> cartOptional = cartRepo.findByCustomerId(customerId);
        return cartOptional.orElseThrow(() -> 
            new RecordNotFoundException("Cart for customer with ID '" + customerId + "' not found"));
    }

    public Cart addOrUpdateCartItems(UUID cartId, Map<UUID, Integer> items) {
        Cart cart = findCartById(cartId);
        List<UUID> invalidProductIds = new ArrayList<>();
    
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();
            if (isValidProductId(productId)) {
                addOrUpdateCartItem(cart, productId, quantity);
            } else {
                invalidProductIds.add(productId);
            }
        }
        saveCart(cart);
        if (!invalidProductIds.isEmpty()) {
            throw new RecordNotFoundException("Products with invalid IDs not updated: " + invalidProductIds.toString());
        }
        return cart;
    }

    private boolean isValidProductId(UUID productId) {
        try {
            productService.findProductById(productId);
            return true;
        } catch (RecordNotFoundException e) {
            return false;
        }
    }

    private void addOrUpdateCartItem(Cart cart, UUID productId, int quantity) {
        int existingQuantity = findExistingCartItem(cart, productId);
        if (existingQuantity >= 0) {
            updateExistingCartItem(cart, productId, quantity);
        } else {
            addNewCartItem(cart, productId, quantity);
        }
    }

    private int findExistingCartItem(Cart cart, UUID productId) {
        return cart.getCartItems().getOrDefault(productId, 0);
    }

    private void updateExistingCartItem(Cart cart, UUID productId, int quantity) {
        int updatedQuantity = cart.getCartItems().getOrDefault(productId, 0) + quantity;
        if (updatedQuantity <= 0) {
            cart.getCartItems().remove(productId);
        } else {
            cart.getCartItems().put(productId, updatedQuantity);
        }
    }

    private void addNewCartItem(Cart cart, UUID productId, int quantity) {
        cart.getCartItems().put(productId, quantity);
    }

    // public void checkoutCart(UUID cartId, String billAndShipAddress, ProductService ProductService) {
    //     this.checkoutCart(cartId, billAndShipAddress, billAndShipAddress, productService);
    // }

    // public void checkoutCart(UUID cartId, String billAddress, String shipAddress, ProductService pServiceroductService) {
    //     // emptyCart(cartId);
    // }

    public Cart emptyCart(UUID cartId) {
        Cart cart = findCartById(cartId);
        cart.getCartItems().clear();
        return saveCart(cart);
    }

    private Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

}
