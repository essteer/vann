package com.vann.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.vann.models.*;
import com.vann.services.CartService;


@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.findAllCarts();
        if (carts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(carts);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/customer/{id}")
    public ResponseEntity<Cart> getCartByCustomerId(@PathVariable UUID id) {
        Cart cart = cartService.createOrFindCartByCustomerId(id);
        return ResponseEntity.ok(cart);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/checkout/{id}")
    public ResponseEntity<Invoice> checkoutCart(
            @PathVariable UUID id,
            @RequestBody Map<String, String> addresses) {
        String billingAddress = addresses.get("billingAddress");
        String shippingAddress = addresses.get("shippingAddress");
        
        if (shippingAddress == null) {
            Invoice invoice = cartService.checkoutCart(id, billingAddress);
            return ResponseEntity.ok(invoice);
        } else {
            Invoice invoice = cartService.checkoutCart(id, billingAddress, shippingAddress);
            return ResponseEntity.ok(invoice);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID id) {
        Cart cart = cartService.findCartById(id);
        return ResponseEntity.ok(cart);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCartItems(
            @PathVariable UUID id,
            @RequestBody Map<UUID, Integer> items) {
        Cart updatedCart = cartService.addOrUpdateCartItems(id, items);
        return ResponseEntity.ok(updatedCart);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> emptyCart(@PathVariable UUID id) {
        Cart cart = cartService.emptyCart(id);
        return ResponseEntity.ok(cart);
    }

}
