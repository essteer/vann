package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.CartItem;
import com.vann.model.Customer;
import com.vann.model.Cart;
import com.vann.repositories.CartRepo;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final CustomerService customerService;

    public CartService(CartRepo cartRepo, CartItemService cartItemService, CustomerService customerService) {
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
        this.customerService = customerService;
    }

    private Cart createCart(Customer customer) {
        Cart newCart = new Cart(customer, new HashSet<>());
        return cartRepo.save(newCart);
    }

    public List<Cart> findAllCarts() {
        return cartRepo.findAll();
    }

    public Cart findCartById(UUID cartId) {
        Optional<Cart> cartOptional = cartRepo.findById(cartId);
        return cartOptional.orElseThrow(() -> 
            new RecordNotFoundException("Cart with ID '" + cartId + "' not found"));
    }

    private Cart findOrCreateCart(
        Supplier<Optional<Cart>> cartFinder, 
        Supplier<Customer> customerSupplier
    ) {
        return cartFinder.get().orElseGet(() -> createCart(customerSupplier.get()));
    }
    
    public Cart findOrCreateCartByCustomerEmail(String email) {
        return findOrCreateCart(
            () -> cartRepo.findByCustomer_CustomerEmail(email),
            () -> customerService.findCustomerByEmail(email)
        );
    }
    
    public Cart findOrCreateCartByCustomerId(UUID customerId) {
        return findOrCreateCart(
            () -> cartRepo.findByCustomer_Id(customerId),
            () -> customerService.findCustomerById(customerId)
        );
    }

    private Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    public Cart addOrUpdateCartItems(UUID cartId, Map<UUID, Integer> items) {
        Cart cart = findCartById(cartId);
            // .orElseThrow(() -> new RecordNotFoundException("Cart with ID '" + cartId + "' not found"));
    
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            UUID cartItemId = entry.getKey();
            int quantity = entry.getValue();
    
            CartItem newItem = cartItemService.findCartItemById(cartItemId);
    
            if (isValidCartItem(cartItemId, cart)) {
                CartItem existingItem = findExistingCartItem(cart, newItem);
    
                if (existingItem != null) {
                    updateExistingCartItem(cart, existingItem, quantity);
                } else {
                    addNewCartItem(cart, newItem, quantity);
                }
            }
        }
        return saveCart(cart);
    }

    private boolean isValidCartItem(UUID cartItemId, Cart cart) {
        return cartItemId.equals(cart.getCartId());
    }

    private CartItem findExistingCartItem(Cart cart, CartItem newItem) {
        return cart.getCartItems().stream()
            .filter(item -> item.getProduct().getProductId().equals(newItem.getProduct().getProductId()))
            .findFirst()
            .orElse(null);
    }

    private void updateExistingCartItem(Cart cart, CartItem existingItem, int quantity) {
        existingItem.setQuantity(existingItem.getQuantity() + quantity);
        if (existingItem.getQuantity() <= 0) {
            cartItemService.deleteCartItem(existingItem.getCartItemId());
            cart.getCartItems().remove(existingItem);
        } else {
            cartItemService.saveCartItem(existingItem);
        }
    }

    private void addNewCartItem(Cart cart, CartItem newItem, int quantity) {
        newItem.setQuantity(quantity);
        newItem.setCart(cart);
        cart.getCartItems().add(newItem);
        cartItemService.saveCartItem(newItem);
    }

    public void checkoutCart(UUID cartId, String billAndShipAddress, CartItemService cartItemService) {
        this.checkoutCart(cartId, billAndShipAddress, billAndShipAddress, cartItemService);
    }

    public void checkoutCart(UUID cartId, String billAddress, String shipAddress, CartItemService cartItemService) {
        // emptyCart(cartId);
    }

    public Cart emptyCart(UUID cartId) {
        Cart cart = findCartById(cartId);
        cart.getCartItems().forEach(cartItem -> cartItemService.deleteCartItem(cartItem.getCartItemId()));
        cart.getCartItems().clear();
        return saveCart(cart);
    }

}
