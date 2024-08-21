package com.vann.service;

import com.vann.model.Cart;
import com.vann.model.CartItem;
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

    public Optional<Cart> findCartById(UUID cartUuid) {
        return cartRepo.findById(cartUuid);
    }

    public Cart updateCart(UUID cartUuid, Cart updatedCart) {
        // Check if the Cart exists
        if (!cartRepo.existsById(cartUuid)) {
            throw new IllegalArgumentException("Cart not found");
        }
        // Set the ID of the updated Cart
        updatedCart.setCartUuid(cartUuid);
        // Save the updated Cart
        return cartRepo.save(updatedCart);
    }

    public void checkoutCart(UUID cartUuid, String billingAndShippingAddress, CartItemService cartItemService) {
        this.checkoutCart(cartUuid, billingAndShippingAddress, billingAndShippingAddress, cartItemService);
    }

    public void checkoutCart(UUID cartUuid, String billingAddress, String shippingAddress, CartItemService cartItemService) {

        // some code
        // this.deleteCart(cartUuid, cartItemService);
    }

    public void deleteCart(UUID cartUuid, CartItemService cartItemService) {
        Optional<Cart> cartOpt = cartRepo.findById(cartUuid);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            for (CartItem cartItem : cart.getCartItems()) {
                cartItemService.deleteCartItem(cartItem.getCartItemUuid());
            }
            cartRepo.deleteById(cartUuid);
        }
    }

}
