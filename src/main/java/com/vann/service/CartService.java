package com.vann.service;

import com.vann.exceptions.CartNotFoundException;
import com.vann.model.CartItem;
import com.vann.model.Cart;
import com.vann.repositories.CartRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepo cartRepo;

    public CartService(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    public Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    public List<Cart> findAllCarts() {
        return cartRepo.findAll();
    }

    public Cart findCartById(UUID cartId) {
        Optional<Cart> cartOptional = cartRepo.findById(cartId);
        return cartOptional.orElseThrow(() -> 
            new CartNotFoundException("Cart with ID " + cartId + " not found"));
    }

    public Cart updateCart(UUID cartId, Cart updatedCart) {
        // Check if the Cart exists
        if (!cartRepo.existsById(cartId)) {
            throw new CartNotFoundException("Cart with ID " + cartId + " not found");
        }
        // Set the ID of the updated Cart
        updatedCart.setCartId(cartId);
        // Save the updated Cart
        return cartRepo.save(updatedCart);
    }

    public void checkoutCart(UUID cartId, String billAndShipAddress, CartItemService cartItemService) {
        this.checkoutCart(cartId, billAndShipAddress, billAndShipAddress, cartItemService);
    }

    public void checkoutCart(UUID cartId, String billAddress, String shipAddress, CartItemService cartItemService) {

        // some code
        // this.deleteCart(cartId, cartItemService);
    }

    public void deleteCart(UUID cartId, CartItemService cartItemService) {
        Optional<Cart> cartOpt = cartRepo.findById(cartId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            for (CartItem cartItem : cart.getCartItems()) {
                cartItemService.deleteCartItem(cartItem.getCartItemId());
            }
            cartRepo.deleteById(cartId);
        }
    }

}
